package com.example.cenitapp.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RunningProofsScreen(navController: NavController) {
    ContentRunningProofsScreen()
}

@Composable
fun ContentRunningProofsScreen() {
    Column(
        modifier =
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Running Proofs")
    }
}

@Preview(showBackground = true)
@Composable
fun RunningProofsScreenPreview() {
    RunningProofsScreen(navController = NavController(LocalContext.current))
}