package com.example.notehub.ui


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notehub.data.db.Note
import com.example.notehub.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NoteViewModel @Inject constructor(
    application: Application,
    private val repository: NotesRepository
) : AndroidViewModel(application) {

    val notes = repository.getAllNotes()

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insert(Note(
                title = title,
                content = content
            ))
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}