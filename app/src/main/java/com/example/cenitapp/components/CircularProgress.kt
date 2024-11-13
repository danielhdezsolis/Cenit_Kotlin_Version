package com.example.cenitapp.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Loading() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(60.dp)
            .padding(16.dp),
        color = Color.Blue, // Puedes cambiar el color
        strokeWidth = 5.dp // Ajusta el grosor del spinner
    )
}
