package com.example.notehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.Navigator
import com.example.notehub.di.NoteViewModel
import com.example.notehub.ui.screens.NoteScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val noteViewModel: NoteViewModel = hiltViewModel()

            Navigator(screen = NoteScreen(noteViewModel))
        }
    }
}
