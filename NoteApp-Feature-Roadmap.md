# NoteApp — Feature Roadmap

A structured guide for evolving the current Fragment + Room + Navigation app into a fully featured note-taking experience.

---

## 🟢 Easy Wins (1–2 hours each)

### 1. Swipe to Delete
Swipe a note left or right in the list to delete it instantly.

**Implementation**
- Attach `ItemTouchHelper` to the `RecyclerView` in `NoteListFragment`
- Call `viewModel.deleteNote()` inside the `onSwiped` callback

```kotlin
ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
    override fun onSwiped(holder: RecyclerView.ViewHolder, dir: Int) {
        val note = adapter.currentList[holder.adapterPosition]
        viewModel.deleteNote(note)
    }
}).attachToRecyclerView(binding.recyclerView)
```

---

### 2. Undo Delete (Snackbar)
Show a short "Note deleted" Snackbar with an **Undo** action after every deletion.

**Implementation**
- Keep a `lastDeleted: Note?` variable in `NoteListViewModel`
- After deletion, call `repository.insert(lastDeleted!!)` when Undo is tapped

---

### 3. Character / Word Count
Show a live character and word count at the bottom of the edit screen.

**Implementation**
- Add a `TextView` below the body field in `fragment_note_edit.xml`
- Attach a `TextWatcher` to `etBody` in `NoteEditFragment`

---

### 4. Share Note
Share the note title + body with any app on the device.

**Implementation**
```kotlin
val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_SUBJECT, note.title)
    putExtra(Intent.EXTRA_TEXT, note.body)
}
startActivity(Intent.createChooser(intent, "Share note via"))
```

---

### 5. Last Modified Timestamp
Track when a note was last edited, not just when it was created.

**Implementation**
- Add `val updatedAt: Long = System.currentTimeMillis()` to the `Note` entity
- Increment Room database version to `2` and provide a migration

---

## 🟡 Medium Complexity (half a day – 1 day each)

### 6. Full-Text Search
Let the user search notes by title or body from the list screen.

**Implementation**
- Add a `SearchView` to `fragment_note_list.xml`
- Add a filtered DAO query:

```kotlin
@Query("SELECT * FROM notes WHERE title LIKE :q OR body LIKE :q ORDER BY createdAt DESC")
fun search(q: String): Flow<List<Note>>
```

- Expose a `searchQuery: MutableStateFlow<String>` in `NoteListViewModel` and `flatMapLatest` it into the notes flow

---

### 7. Sort Options
Allow sorting by creation date, last modified date, or title (A–Z).

**Implementation**
- Add a `SortOrder` enum: `DATE_CREATED`, `DATE_MODIFIED`, `TITLE`
- Add matching DAO queries for each order
- Expose a `sortOrder: MutableStateFlow<SortOrder>` in `NoteListViewModel`
- Add a sort menu item via `onCreateOptionsMenu` in `NoteListFragment`

---

### 8. Pin Important Notes
Pin notes to always appear at the top of the list.

**Implementation**
- Add `val isPinned: Boolean = false` to the `Note` entity
- Update the DAO query: `ORDER BY isPinned DESC, createdAt DESC`
- Show a pin icon on the card; tap it to toggle pinned state
- Add a `pinNote(note: Note)` function to `NoteEditViewModel`

---

### 9. Note Color Labels
Color-code note cards for quick visual categorisation.

**Implementation**
- Add `val color: Int = 0` to `Note` (store as a color int or index 0–5)
- Show a row of color circles in the edit screen
- Apply the color to `MaterialCardView`'s `setCardBackgroundColor()`

---

### 10. Archive Notes
Move notes out of the main list without permanently deleting them.

**Implementation**
- Add `val isArchived: Boolean = false` to `Note`
- Filter the main list: `WHERE isArchived = 0`
- Add an **Archive** screen/destination with: `WHERE isArchived = 1`
- Long-press or menu item to archive / unarchive

---

### 11. Dark Mode Toggle
Let users switch between light and dark themes from settings.

**Implementation**
```kotlin
// Light
AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
// Dark
AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
// Follow system
AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
```
- Persist the choice in `SharedPreferences` or `DataStore`

---

## 🔴 Advanced Features (multiple days)

### 12. Reminders & Notifications
Let users set a date/time reminder on any note.

**Implementation**
- Add `val reminderAt: Long? = null` to `Note`
- Use `AlarmManager` to schedule an exact alarm
- Show a `NotificationCompat` notification when the alarm fires
- Request `POST_NOTIFICATIONS` permission on Android 13+

---

### 13. Trash / Recycle Bin
Recover accidentally deleted notes within 30 days.

**Implementation**
- Add `val deletedAt: Long? = null` to `Note`
- Soft-delete: set `deletedAt` instead of calling `dao.delete()`
- Add a **Trash** screen showing soft-deleted notes
- Use `WorkManager` to permanently purge notes older than 30 days

---

### 14. Markdown Rendering
Render **bold**, *italic*, `code`, and bullet lists inside note bodies.

**Recommended library:** [Markwon](https://github.com/noties/Markwon)

```kotlin
val markwon = Markwon.create(requireContext())
markwon.setMarkdown(binding.tvBody, note.body)
```

---

### 15. Export / Backup
Export all notes as a single JSON file or individual `.txt` files.

**Implementation**
- Use the **Storage Access Framework** (`Intent.ACTION_CREATE_DOCUMENT`) to let the user pick a save location
- Serialize notes with `kotlinx.serialization` or `Gson`
- Provide an import option that reads the file back into Room

---

### 16. Home Screen Widget
Show the most recent or pinned notes on the Android home screen.

**Implementation**
- Create an `AppWidgetProvider` subclass
- Use `RemoteViews` to display note titles
- Update the widget via `AppWidgetManager` whenever notes change

---

### 17. Biometric Lock
Require fingerprint or face authentication before the app opens.

**Implementation**
```kotlin
BiometricPrompt(this, executor, callback).authenticate(promptInfo)
```
- Add the `androidx.biometric:biometric` dependency
- Show the prompt in `MainActivity.onCreate()`

---

### 18. Dependency Injection with Hilt
Replace manual constructor wiring with Hilt for cleaner, testable code.

**Implementation**
- Add the Hilt Gradle plugin and dependency
- Annotate `NoteDatabase`, `NoteRepository` with `@Singleton` / `@Provides`
- Replace `AndroidViewModel` with `@HiltViewModel` + `@Inject constructor`

---

## 📋 Recommended Implementation Order

```
Phase 1 — Core UX
  ✦ Swipe to delete + Undo
  ✦ Full-text search
  ✦ Sort options

Phase 2 — Organisation
  ✦ Pin notes
  ✦ Archive
  ✦ Color labels
  ✦ Last modified timestamp

Phase 3 — Power Features
  ✦ Reminders & notifications
  ✦ Trash / recycle bin
  ✦ Dark mode toggle

Phase 4 — Polish & Scale
  ✦ Markdown rendering
  ✦ Export / backup
  ✦ Home screen widget
  ✦ Biometric lock
  ✦ Hilt DI
```

---

> **Tip:** Implement Phase 1 first — search and swipe-to-delete alone make the app feel production-ready for daily use.

