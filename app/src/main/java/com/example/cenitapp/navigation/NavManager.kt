package com.example.cenitapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cenitapp.views.AccountScreen
import com.example.cenitapp.views.CertificatesScreen
import com.example.cenitapp.views.DetailsScreen
import com.example.cenitapp.views.DevicesScreen
import com.example.cenitapp.views.NotificationsScreen
import com.example.cenitapp.views.OnBoardingScreen
import com.example.cenitapp.views.RunningProofsScreen

@Composable
fun NavManager(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "RunningProofs"
    ) {
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
        composable("Details") {
            DetailsScreen(navController)
        }
    }
}