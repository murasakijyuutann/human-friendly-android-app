# Add project specific ProGuard rules here.

# Preserve stack trace line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ---- Room ----
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.paging.**

# ---- Navigation Safe Args ----
-keep class com.example.noteapp.ui.notes.**Args { *; }
-keep class com.example.noteapp.ui.notes.**Directions { *; }

# ---- Kotlin ----
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# ---- Coroutines ----
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

