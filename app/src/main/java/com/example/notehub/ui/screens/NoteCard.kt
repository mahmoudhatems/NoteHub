package com.example.notehub.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notehub.data.db.Note

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = if (note.isFavorite) Color(0xFFFFC7EA) else Color(0xFFCAEDFF)),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .clickable(onClick = onFavoriteClick)
                    .align(Alignment.End)
                    .size(24.dp),
                imageVector = if (note.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = if (note.isFavorite) Color(0xFFD8B4F8) else Color(0xFFB0BEC5)
            )
            Text(
                text = note.title,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}