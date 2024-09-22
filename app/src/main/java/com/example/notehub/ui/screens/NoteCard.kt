package com.example.notehub.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notehub.data.db.Note
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Remembering state for favorite status
    val isFavorite = remember { mutableStateOf(note.isFavorite) }
    val favoriteIconScale by animateFloatAsState(
        targetValue = if (isFavorite.value) 1.3f else 1.0f,
        animationSpec = tween(durationMillis = 300)
    )
    val favoriteIconColor by animateColorAsState(
        targetValue = if (isFavorite.value) Color(0xFFFF9900) else Color(0xFFB0BEC5),
        animationSpec = tween(durationMillis = 300)
    )

    // Change background color based on favorite state
    val cardBackgroundColor = if (isFavorite.value) Color(0xFFFFF3E0) else Color(0xFF1E1E1E)

    // Change text colors based on favorite state
    val titleColor = if (isFavorite.value) Color(0xFF000000) else Color(0xFFFFFFFF)
    val descriptionColor = if (isFavorite.value) Color(0xFF424242) else Color(0xFFB0BEC5)

    OutlinedCard(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
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
                imageVector = if (isFavorite.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = favoriteIconColor,
                modifier = Modifier
                    .clickable {
                        isFavorite.value = !isFavorite.value // Toggle favorite state
                        onFavoriteClick()
                    }
                    .scale(favoriteIconScale)
                    .align(Alignment.End)
                    .size(24.dp)
            )
            Text(
                text = note.title,
                fontSize = 18.sp,
                color = titleColor,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.description,
                fontSize = 14.sp,
                color = descriptionColor,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}