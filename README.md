# 📝 NoteApp

🌐 [日本語版はこちら](README_JA.md)

A clean, minimal note-taking Android app built with **Kotlin** and modern Android architecture components. NoteApp is designed as a solid, well-structured foundation — easy to understand, easy to extend.

---

## ✨ Features

| Feature | Details |
|---|---|
| 🎉 Welcome Screen | Full-screen splash that greets the user; tap anywhere to enter the app |
| 📋 Note List | All notes displayed newest-first in a scrollable list |
| ➕ Create Note | Add a new note with a title and body |
| ✏️ Edit Note | Tap any note to open it and make changes |
| 🗑️ Delete Note | Remove a note from the edit screen |
| ✅ Input Validation | Empty titles are caught and surfaced with a clear error message |
| 💾 Persistent Storage | All notes survive app restarts — stored locally with Room |
| 🎨 Material Design 3 | Follows the Material You design system with DayNight theme support |

---

## 🏗️ Architecture

NoteApp follows **MVVM (Model-View-ViewModel)** with a clean separation of layers:

```
app/
└── data/
│   ├── model/          # Note entity (Room @Entity)
│   ├── local/          # Room DAO + Database
│   └── repository/     # NoteRepository — single source of truth
└── ui/
    └── notes/          # Fragments + ViewModels for each screen
```

### Layer responsibilities

- **Model** (`Note.kt`) — Plain data class annotated for Room
- **DAO** (`NoteDao.kt`) — SQL queries exposed as `suspend fun` and `Flow`
- **Repository** (`NoteRepository.kt`) — Abstracts the data source from the UI
- **ViewModel** (`NoteListViewModel`, `NoteEditViewModel`) — Holds UI state, survives rotation
- **Fragment** — Observes the ViewModel, drives the UI; zero business logic

---

## 🛠️ Tech Stack

| Library | Version | Purpose |
|---|---|---|
| Kotlin | 2.1.20 | Primary language |
| Room | 2.7.0 | Local SQLite persistence |
| Navigation Component | 2.9.7 | Fragment navigation + back-stack |
| Safe Args | 2.9.7 | Type-safe navigation arguments |
| Kotlin Coroutines | 1.10.2 | Async operations |
| Kotlin Flow | — | Reactive note stream |
| ViewModel / LiveData | 2.10.0 | Lifecycle-aware UI state |
| View Binding | — | Null-safe view access |
| Material Design 3 | 1.12.0 | UI components and theming |
| RecyclerView | 1.4.0 | Efficient note list rendering |

---

## 🔍 What Makes It Stand Out

### 1. Reactive data pipeline
Notes flow from the Room database through a Kotlin `Flow` all the way to the UI — no manual refresh calls needed. When a note is saved or deleted, the list updates automatically.

```kotlin
// NoteDao.kt
@Query("SELECT * FROM notes ORDER BY createdAt DESC")
fun getAll(): Flow<List<Note>>
```

### 2. Type-safe navigation with Safe Args
Arguments between screens are passed as typed objects, not raw `Bundle` strings. This catches mistakes at compile time instead of at runtime.

```kotlin
// NoteListFragment.kt
val action = NoteListFragmentDirections
    .actionNoteListFragmentToNoteEditFragment(note.id.toInt())
findNavController().navigate(action)
```

### 3. Singleton Room database with thread safety
The database instance uses `@Volatile` + a double-checked `synchronized` block — a production-grade pattern that prevents race conditions in multi-threaded environments.

### 4. Single-Activity architecture
One `MainActivity`, all navigation handled by the Navigation Component. Clean back-stack management with zero `FragmentTransaction` boilerplate.

### 5. Release-ready build configuration
Production builds ship with code minification (`isMinifyEnabled = true`) and resource shrinking (`isShrinkResources = true`) already enabled — no extra setup needed before publishing.

---

## 🚀 Getting Started

### Requirements
- Android Studio Hedgehog or later
- Android SDK 24+ (Android 7.0 Nougat and above)
- JDK 17

### Run the app
1. Clone or download the project
2. Open in Android Studio
3. Let Gradle sync finish
4. Run on an emulator or physical device (API 24+)

---

## 🗺️ Roadmap

The project ships with a full [`NoteApp-Feature-Roadmap.md`](NoteApp-Feature-Roadmap.md) covering 18 planned features across four phases:

| Phase | Highlights |
|---|---|
| 🟢 Easy Wins | Swipe to delete, undo, share note, word count, timestamps |
| 🟡 Medium | Full-text search, sort options, pin notes, color labels, archive, dark mode |
| 🔴 Advanced | Reminders, trash/recycle bin, Markdown rendering, export/backup |
| 🔮 Polish | Home screen widget, biometric lock, Hilt dependency injection |

---

## 📁 Project Structure

```
NoteApp/
├── app/src/main/
│   ├── java/com/example/noteapp/
│   │   ├── data/
│   │   │   ├── local/        NoteDao.kt, NoteDatabase.kt
│   │   │   ├── model/        Note.kt
│   │   │   └── repository/   NoteRepository.kt
│   │   ├── ui/notes/
│   │   │   ├── WelcomeFragment.kt
│   │   │   ├── NoteListFragment.kt + NoteListViewModel.kt
│   │   │   ├── NoteEditFragment.kt + NoteEditViewModel.kt
│   │   │   └── NoteAdapter.kt
│   │   └── MainActivity.kt
│   └── res/
│       ├── layout/           fragment_welcome.xml, fragment_note_list.xml, ...
│       ├── navigation/       nav_graph.xml
│       └── values/           strings.xml, themes.xml, colors.xml
├── NoteApp-Feature-Roadmap.md
└── README.md
```

---

## 📄 License

This project is open for learning, experimentation, and personal use.


