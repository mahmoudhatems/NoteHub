package com.example.notehub.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.notehub.data.db.Note
import com.example.notehub.di.NoteViewModel
import com.example.notehub.ui.SwipeToDeleteContainer

@Composable
fun HomeScreenContent(
    noteViewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit
) {
    val allNotes by noteViewModel.allNotes.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // Check if the list of notes is empty
    if (allNotes.isNullOrEmpty()) {
        EmptyNotesScreen() // Show the empty notes screen
    } else {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(allNotes ?: emptyList()) { note ->
                SwipeToDeleteContainer(item = note, onDelete = {
                    noteViewModel.deleteNote(it)
                }) {
                    NoteCard(
                        note = note,
                        onClick = {
                            selectedNote = note
                            showDialog = true
                        },
                        onFavoriteClick = {
                            noteViewModel.updateNote(note.copy(isFavorite = !note.isFavorite))
                        }
                    )
                }
            }
        }
    }

    if (showDialog && selectedNote != null) {
        NoteDetailsDialog(
            note = selectedNote!!,
            noteViewModel = noteViewModel,
            onDismissRequest = { showDialog = false }
        )
    }
}