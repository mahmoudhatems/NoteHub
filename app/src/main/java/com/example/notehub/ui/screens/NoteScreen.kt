package com.example.notehub.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.notehub.data.db.Note
import com.example.notehub.di.NoteViewModel


class NoteScreen(val noteViewModel: NoteViewModel) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val selectedIndex = remember { mutableStateOf(0) }
        val showAddDialog = remember { mutableStateOf(false) }


            Scaffold(
                floatingActionButton = {
                    IconButton(modifier = Modifier.size(80.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray, contentColor = Color.White),
                        onClick = {
                            showAddDialog.value = true
                        }) {
                        Icon(modifier = Modifier.size(35.dp),imageVector = Icons.Default.Add, contentDescription =null)
                    }
                },
                topBar = { TopAppBar(
                    actions = {
                        // Action buttons
                        IconButton(onClick = {  }) {
                            Icon(Icons.Default.MoreVert, contentDescription =null, modifier = Modifier.size(50.dp),tint = Color.White )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray, titleContentColor = Color.White)
                    ,
                    title = { Text(text = "MyNotes", fontSize = 30.sp) })
                }
                , bottomBar = { BottomAppBar(containerColor = Color.Gray) {
                    IconButton(modifier = Modifier.weight(1f),onClick = { selectedIndex.value = 0 }) {
                        Icon(modifier = Modifier.size(50.dp),imageVector = Icons.Filled.Home, contentDescription =null, tint = if(selectedIndex.value==0) Color.Black else  Color.White )
                    }
                    IconButton(modifier = Modifier.weight(1f),onClick = { selectedIndex.value = 1  }) {
                        Icon(modifier = Modifier.size(50.dp),imageVector = Icons.Default.Favorite, contentDescription =null, tint = if(selectedIndex.value==1) Color.Black else  Color.White )
                    }

                }
                }
                ,modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    // screen content
                    if(showAddDialog.value){
                        AddNoteDialog(onSaveClick = { x , y ->
                            noteViewModel.insertNote(Note(title = x, description =y , isFavorite = false))
                            showAddDialog.value = false
                        },
                            onDismissRequest = {
                                showAddDialog.value= false
                            })
                    }
                    if (selectedIndex.value == 0) {
                        HomeScreenContent(noteViewModel = noteViewModel)
                    } else {
                        FavoriteScreenContent(noteViewModel = noteViewModel)
                    }

                }
            }
        }
    }

@Composable
fun AddNoteDialog(onSaveClick : (String,String)->Unit,onDismissRequest : ()->Unit){
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = { onDismissRequest() },
        icon = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable { onDismissRequest() }) {
                Icon(Icons.Default.Close, contentDescription =null , modifier = Modifier.align(
                    Alignment.CenterEnd))

            }
        },
        confirmButton = {
            Button(onClick = { onSaveClick(title.value,description.value) }) {
                Text(text ="Save Note" )
            }
        },
        title = { Text(text = "Add New Note")},
        text = {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(value = title.value, onValueChange = {title.value=it},
                    placeholder = { Text(text = "Add Title")})
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(modifier = Modifier.height(100.dp),value = description.value, onValueChange = {description.value=it},
                    placeholder = { Text(text = "Add Description")})
            }
        }

    )
}
