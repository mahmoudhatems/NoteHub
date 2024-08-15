package com.example.notehub.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint


@Composable
fun NotesApp() {
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NotesNavHost(navController = navController)
    }
}

@Composable
fun NotesNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "notes") {
        composable("notes") {
            NoteScreen()
        }
    }
}