package com.example.noteapp.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.local.NoteDatabase
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class NoteListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    val notes: StateFlow<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        notes = repository.allNotes
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    }
}

