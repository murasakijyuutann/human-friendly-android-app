package com.example.noteapp.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.local.NoteDatabase
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteEditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    private val _note = MutableLiveData<Note?>()
    val note: LiveData<Note?> = _note

    init {
        val dao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
    }

    fun loadNote(id: Long) {
        viewModelScope.launch {
            _note.value = repository.getById(id)
        }
    }

    fun saveNote(title: String, body: String, existing: Note?) {
        viewModelScope.launch {
            if (existing != null) {
                repository.update(existing.copy(title = title, body = body))
            } else {
                repository.insert(Note(title = title, body = body))
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}

