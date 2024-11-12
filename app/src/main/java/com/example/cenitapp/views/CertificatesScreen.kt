package com.example.cenitapp.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CertificatesScreen(navController: NavController) {
    ContentCertificatesScreen(navController)
}

@Composable
fun ContentCertificatesScreen(navController: NavController) {
    val id_proof = "123456789"
    Column(
        modifier =
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Certificates Screen")
        Button(onClick = {navController.navigate("Details/${id_proof}")}, modifier = Modifier.align(Alignment.CenterHorizontally)){
            Text(text = "Detalles")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CertificatesScreenPreview() {
    CertificatesScreen(navController = NavController(LocalContext.current))
}