package com.example.cenitapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cenitapp.SupabaseAuthViewModel
import com.example.cenitapp.views.AccountScreen
import com.example.cenitapp.views.BlankScreen
import com.example.cenitapp.views.CertificatesScreen
import com.example.cenitapp.views.DetailsScreen
import com.example.cenitapp.views.DevicesScreen
import com.example.cenitapp.views.LoginScreen
import com.example.cenitapp.views.NotificationsScreen
import com.example.cenitapp.views.OnBoardingScreen
import com.example.cenitapp.views.RunningProofsScreen

@Composable
fun NavManager(navController: NavHostController, viewModel: SupabaseAuthViewModel) {
    NavHost(
        navController = navController,
        startDestination = "BlankScreen"
    ) {
        composable("Login") {
            LoginScreen(navController, viewModel)
        }
        composable("RunningProofs") {
            RunningProofsScreen(navController)
        }
        composable("Certificates") {
            CertificatesScreen(navController)
        }
        composable("Devices") {
            DevicesScreen(navController)
        }
        composable("Notifications") {
            NotificationsScreen(navController)
        }
        composable("Account") {
            AccountScreen(navController)
        }
        composable("Onboarding") {
            OnBoardingScreen(navController)
        }
        composable(
            "Details/{id_proof}",
            arguments = listOf(navArgument("id_proof") { type = NavType.StringType },)
        ) {
            val idProof = it.arguments?.getString("id_proof") ?: ""
            DetailsScreen(navController, idProof)
        }
        composable("BlankScreen"){
            BlankScreen(navController, viewModel)
        }
    }
}