package com.example.cenitapp.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cenitapp.R

@Composable
fun CustomFab(navController: NavHostController) {
    FloatingActionButton(
        onClick = {
            navController.navigate("Onboarding")
        }, containerColor = colorResource(R.color.primary),
        shape = CircleShape,
        modifier = Modifier
            .size(40.dp)
            .offset(y = 38.dp)
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "FAB",
            tint = colorResource(R.color.background)
        )
    }
}