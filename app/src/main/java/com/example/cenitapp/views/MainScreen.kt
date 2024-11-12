package com.example.cenitapp.views

import android.annotation.SuppressLint
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cenitapp.components.CustomBottomNavigation
import com.example.cenitapp.components.CustomFab
import com.example.cenitapp.components.CustomToolbar
import com.example.cenitapp.navigation.NavManager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isOnboardingScreen = currentBackStackEntry?.destination?.route == "Onboarding"

    Scaffold(
        topBar = {
            if (!isOnboardingScreen) {
                CustomToolbar(navController)
            }
        },
        floatingActionButton = {
            if (!isOnboardingScreen) {
                CustomFab(navController)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (!isOnboardingScreen) {
                CustomBottomNavigation(navController)
            }
        }
    ) {
        NavManager(navController)
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}