package com.example.notehub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.notehub.data.db.Note
import com.example.notehub.di.NoteViewModel

class NoteScreen(val noteViewModel: NoteViewModel) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val selectedIndex = remember { mutableStateOf(0) }
        val showAddDialog = remember { mutableStateOf(false) }
        val showNoteDialog = remember { mutableStateOf<Note?>(null) }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog.value = true },
                    containerColor = Color(0xFF6200EE) // Purple 500
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note", tint = Color.White)
                }
            },
            topBar = {
                TopAppBar(
                    actions = {
                        IconButton(onClick = { /* Handle more actions */ }) {
                            Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE)), // Purple 500
                    title = { Text(text = "NotesHub", fontSize = 30.sp, color = Color.White) }
                )
            },
            bottomBar = {
                BottomAppBar(containerColor = Color(0xFF6200EE)) { // Purple 500
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { selectedIndex.value = 0 }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = if (selectedIndex.value == 0) Color.White else Color(0xFFB0BEC5) // White if selected, Grey otherwise
                        )
                    }
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { selectedIndex.value = 1 }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = if (selectedIndex.value == 1) Color.White else Color(0xFFB0BEC5) // White if selected, Grey otherwise
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                // Show add note dialog
                if (showAddDialog.value) {
                    AddNoteDialog(
                        onSaveClick = { title, description ->
                            noteViewModel.insertNote(
                                Note(
                                    title = title,
                                    description = description,
                                    isFavorite = false
                                )
                            )
                            showAddDialog.value = false
                        },
                        onDismissRequest = { showAddDialog.value = false }
                    )
                }

                // Show note details dialog
                showNoteDialog.value?.let { note ->
                    NoteDetailsDialog(
                        note = note,
                        noteViewModel = noteViewModel,
                        onDismissRequest = { showNoteDialog.value = null }
                    )
                }

                if (selectedIndex.value == 0) {
                    HomeScreenContent(
                        noteViewModel = noteViewModel,
                        onNoteClick = { note -> showNoteDialog.value = note }
                    )
                } else {
                    FavoriteScreenContent(
                        noteViewModel = noteViewModel,
                        onNoteClick = { note -> showNoteDialog.value = note }
                    )
                }
            }
        }
    }
}

@Composable
fun AddNoteDialog(
    onSaveClick: (String, String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = "Add New Note", color = Color(0xFF6200EE)) }, // Purple 500
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    placeholder = { Text(text = "Add Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    placeholder = { Text(text = "Add Description") },
                    modifier = Modifier.height(100.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSaveClick(title.value, description.value) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Purple 500
            ) {
                Text(text = "Save Note", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)) // Grey
            ) {
                Text(text = "Cancel", color = Color.White)
            }
        }
    )
}

@Composable
fun NoteDetailsDialog(
    note: Note,
    noteViewModel: NoteViewModel,
    onDismissRequest: () -> Unit
) {
    val title = remember { mutableStateOf(note.title) }
    val description = remember { mutableStateOf(note.description) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Note Details", color = Color(0xFF6200EE)) }, // Purple 500
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text(text = "Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text(text = "Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = MaterialTheme.shapes.medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    noteViewModel.updateNote(
                        note.copy(title = title.value, description = description.value)
                    )
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Purple 500
            ) {
                Text(text = "Update", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)) // Grey
            ) {
                Text(text = "Dismiss", color = Color.White)
            }
        }
    )
}
