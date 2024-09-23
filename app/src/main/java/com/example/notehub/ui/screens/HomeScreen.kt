package com.example.notehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notehub.data.db.Note
import com.example.notehub.di.NoteViewModel
import com.example.notehub.ui.SwipeToDeleteContainer
import com.example.notehub.ui.theme.mainBlack

@Composable
fun HomeScreenContent(
    noteViewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit
) {
    val allNotes by noteViewModel.allNotes.collectAsState()
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    if (allNotes.isNullOrEmpty()) {
        EmptyNotesScreen()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp), // Adjusted for better layout
            modifier = Modifier
                .fillMaxWidth()
                .background(color = mainBlack),
            contentPadding = PaddingValues(16.dp), // Increased padding for better spacing
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth() // Ensure card takes full width of the cell
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
