package com.example.notehub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.notehub.data.db.Note
import com.example.notehub.di.NoteViewModel

class NoteDetailsScreen(val note: Note, val noteViewModel: NoteViewModel) : Screen {
    @Composable
    override fun Content() {
        val title = remember { mutableStateOf(note.title) }
        val description = remember { mutableStateOf(note.description) }
        val navigator = LocalNavigator.currentOrThrow

        // Create a mutable state to control the dialog visibility
        val showDialog = remember { mutableStateOf(true) }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "Note Details") },
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
                            placeholder = { Text(text = "Add Title") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )

                        OutlinedTextField(
                            value = description.value,
                            onValueChange = { description.value = it },
                            placeholder = { Text(text = "Add Description") },
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
                            showDialog.value = false
                        }
                    ) {
                        Text(text = "Update")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            noteViewModel.deleteNote(note)
                            showDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Delete")
                    }
                }
            )
        }
    }
}
