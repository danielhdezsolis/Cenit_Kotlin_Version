package com.example.cenitapp.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.cenitapp.SupabaseAuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cenitapp.data.model.UserState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
//fun AccountScreen(navController: NavController, viewModel: SupabaseAuthViewModel = viewModel()) {
//    ContentAccountScreen( navController, viewModel)
//}
//
//@Composable
//fun ContentAccountScreen( navController: NavController, viewModel: SupabaseAuthViewModel) {
//    val userState by viewModel.userState
//    val context = LocalContext.current
//    Column(
//        modifier =
//        Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Account Screen")
//        Button(
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
//            onClick = {
//                viewModel.logout(context)
//                navController.navigate("BlankScreen")
//            }) {
//            Text(text = "Cerrar sesión")
//        }
//    }
//}
fun AccountScreen(navController: NavController, viewModel: SupabaseAuthViewModel = viewModel()) {
    val userState by viewModel.userState
    val context = LocalContext.current

    // Observa el estado de usuario para redirigir después de cerrar sesión
    LaunchedEffect(userState) {
        if (userState == UserState.NotAuthenticated) {
            // Cuando el usuario no está autenticado, redirige a BlankScreen
            navController.navigate("BlankScreen") {
                popUpTo("Account") { inclusive = true }
            }
        }
    }

    ContentAccountScreen(navController, viewModel)
}

@Composable
fun ContentAccountScreen(navController: NavController, viewModel: SupabaseAuthViewModel) {
    val context = LocalContext.current
    Column(
        modifier =
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Account Screen")
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            onClick = {
                // Realiza el logout y luego espera a que el estado cambie
                viewModel.logout(context)
            }) {
            Text(text = "Cerrar sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(navController = NavController(LocalContext.current))
}