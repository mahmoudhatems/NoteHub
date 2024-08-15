package com.example.notehub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.notehub.di.NoteViewModel
import com.example.notehub.data.db.Note
@Composable
fun FavoriteScreenContent(
    noteViewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit
) {
    val allNotes = noteViewModel.allNotes.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(allNotes.value?.filter { it.isFavorite } ?: emptyList()) { note ->
            NoteCard(
                note = note,
                onClick = {
                    onNoteClick(note) // Call onNoteClick when a note is clicked
                },
                onFavoriteClick = {
                    noteViewModel.updateNote(note.copy(isFavorite = !note.isFavorite))
                }
            )
        }
    }
}