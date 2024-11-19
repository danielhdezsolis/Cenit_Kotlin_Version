package com.example.cenitapp.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cenitapp.components.Loading
import com.example.cenitapp.viewModels.RunningProofsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RunningProofsScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RunningProofsViewModel = viewModel()


    // Ejecutar la consulta cuando se navegue a esta pantalla
    LaunchedEffect(Unit) {
        viewModel.fetchProofs(context)
    }

    ContentRunningProofsScreen(viewModel)
}

@Composable
fun ContentRunningProofsScreen(viewModel: RunningProofsViewModel) {
    val proofs by viewModel.proofs
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Running Proofs")

        when {
            isLoading -> Loading()
            errorMessage != null -> Text(text = errorMessage ?: "Unknown error")
            proofs.isEmpty() -> Text(text = "No hay datos disponibles.")
            else -> {
                // Renderizar la lista de proofs
                proofs.forEach { proof ->
                    Text(text = "Proof ID: ${proof.id}, Status: ${proof.status}, Date: ${proof.created_at}")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RunningProofsScreenPreview() {
    RunningProofsScreen(navController = NavController(LocalContext.current))
}