package com.example.notehub.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.notehub.data.db.Note
import com.example.notehub.di.NoteViewModel

class NoteDetailsScreen(val note: Note, val noteViewModel: NoteViewModel) : Screen{
    @Composable
    override fun Content() {
        val title = remember { mutableStateOf(note.title) }
        val description = remember { mutableStateOf(note.description) }
        val navigator = LocalNavigator.currentOrThrow
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value = title.value, onValueChange = {title.value=it},
                placeholder = { Text(text = "Add Title") })
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(modifier = Modifier.height(100.dp),value = description.value, onValueChange = {description.value=it},
                placeholder = { Text(text = "Add Description") })

            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Button(onClick = {
                    noteViewModel.updateNote(note.copy(title = title.value, description = description.value))
                    navigator.pop()
                }) {
                    Text(text = "Update")
                }

                Button(onClick = {
                    noteViewModel.deleteNote(note)
                    navigator.pop()
                }) {
                    Text(text = "Delete")
                }
            }

        }

    }

}