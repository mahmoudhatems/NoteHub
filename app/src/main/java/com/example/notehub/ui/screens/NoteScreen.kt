package com.example.notehub.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.notehub.R
import com.example.notehub.data.db.Note
import com.example.notehub.di.NoteViewModel
import com.example.notehub.ui.theme.pornhubBlack
import com.example.notehub.ui.theme.pornhubGray
import com.example.notehub.ui.theme.pornhubOrange
import com.example.notehub.ui.theme.pornhubWhite

class NoteScreen(val noteViewModel: NoteViewModel) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val selectedIndex = remember { mutableStateOf(0) }
        val showAddDialog = remember { mutableStateOf(false) }
        val showNoteDialog = remember { mutableStateOf<Note?>(null) }

        Scaffold(
            containerColor = pornhubBlack,
           modifier = Modifier.background(color =  pornhubBlack),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog.value = true },
                    containerColor = pornhubOrange,
                    shape = MaterialTheme.shapes.large,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier
                        .offset(y = (60).dp) // Moves the FAB upwards to overlap the BottomAppBar
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Note",
                        tint = pornhubWhite  // White textcolor = pornhubWhite
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
           //  isFloatingActionButtonDocked = true, // Make FAB dock to BottomAppBar
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "NotesHub",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = pornhubWhite  // White text
                        )
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = pornhubWhite)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = pornhubBlack)
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = pornhubGray,
                    modifier = Modifier
                        .height(60.dp)
                        .clip(RectangleShape)
                ) {
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { selectedIndex.value = 0 }
                    ){
                        val color by animateColorAsState(
                            if (selectedIndex.value == 0) pornhubOrange
                            else Color.Gray
                        )
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = color
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.4f)) // Center alignment for FAB

                    // Favorite Button
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { selectedIndex.value = 1 }
                    ) {
                        val color by animateColorAsState(
                            if (selectedIndex.value == 1) pornhubOrange
                            else Color.Gray
                        )
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = color
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(pornhubBlack)
                    .clip(MaterialTheme.shapes.medium)
            ) {
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
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Add New Note", color = pornhubOrange) },
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
                colors = ButtonDefaults.buttonColors(containerColor =pornhubOrange)
            ) {
                Text(text = "Save Note", color =pornhubWhite)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(containerColor = pornhubWhite)
            ) {
                Text(text = "Cancel", color = pornhubBlack)
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
        title = { Text(text = "Note Details", color = pornhubOrange) },
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
                colors = ButtonDefaults.buttonColors(containerColor =pornhubOrange)
            ) {
                Text(text = "Update", color =pornhubWhite)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(containerColor = pornhubGray)
            ) {
                Text(text = "Dismiss", color =pornhubWhite)
            }
        }
    )
}


@Composable
fun EmptyNotesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.rafiki),
                contentDescription = "Empty Notes",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                     .height(300.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = "Create your first note!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp),
                color = Color.White
            )
        }
    }
}
