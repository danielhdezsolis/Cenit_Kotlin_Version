package com.example.cenitapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cenitapp.SupabaseAuthViewModel
import com.example.cenitapp.components.Loading
import com.example.cenitapp.data.model.UserState
import kotlinx.coroutines.delay

@Composable
//fun BlankScreen(navController: NavController, viewModel: SupabaseAuthViewModel = viewModel()) {
//    val userState by viewModel.userState
//    val context = LocalContext.current
//
//    // Llamar a la función isUserLoggedIn solo cuando la pantalla se muestra
//    LaunchedEffect(Unit) {
//        viewModel.isUserLoggedIn(context)
//    }
//
//    // Observa el estado del usuario y redirige en consecuencia
//    LaunchedEffect(userState) {
//        when (userState) {
//            is UserState.Authenticated -> {
//                navController.navigate("RunningProofs") {
//                    popUpTo("BlankScreen") { inclusive = true }
//                }
//            }
//            UserState.NotAuthenticated -> {
//                navController.navigate("Login") {
//                    popUpTo("BlankScreen") { inclusive = true }
//                }
//            }
//            else -> {
//                // Mantener en BlankScreen para los otros estados como Loading o Error
//            }
//        }
//    }
//}
fun BlankScreen(navController: NavController, viewModel: SupabaseAuthViewModel = viewModel()) {
    val userState by viewModel.userState
    val context = LocalContext.current

    // Llamar a la función isUserLoggedIn solo cuando la pantalla se muestra
    LaunchedEffect(Unit) {
        delay(10)
        viewModel.isUserLoggedIn(context)
    }

    // Contenido de la pantalla
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostrar el Loading spinner cuando el estado sea Loading
        if (userState == UserState.Loading) {
            Loading() // Muestra el spinner de carga
        }

        // Observa el estado del usuario y redirige en consecuencia
        LaunchedEffect(userState) {
            when (userState) {
                is UserState.Authenticated -> {
                    navController.navigate("RunningProofs") {
                        popUpTo("BlankScreen") { inclusive = true }
                    }
                }
                UserState.NotAuthenticated -> {
                    navController.navigate("Login") {
                        popUpTo("BlankScreen") { inclusive = true }
                    }
                }
                else -> {
                    // Mantener en BlankScreen para los otros estados como Loading o Error
                }
            }
        }
    }
}
