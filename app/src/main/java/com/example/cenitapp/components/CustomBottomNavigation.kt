package com.example.cenitapp.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.cenitapp.R

@Composable
fun CustomBottomNavigation(navController: NavHostController) {
    val selectedIconColor = colorResource(R.color.text)
    val unselectedIconColor = colorResource(R.color.secondary)
    val selectedIndicatorColor = colorResource(R.color.transparentColor)
    val backgroundColor = colorResource(R.color.tab)
    NavigationBar(
        containerColor = backgroundColor,
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painterResource(R.drawable.thermometer),
                    contentDescription = "Running Proofs"
                )
            },
            selected = navController.currentDestination?.route == "RunningProofs",
            onClick = { navController.navigate("RunningProofs") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                indicatorColor = selectedIndicatorColor
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painterResource(R.drawable.draft),
                    contentDescription = "Certificates"
                )
            },
            selected = navController.currentDestination?.route == "Certificates",
            onClick = { navController.navigate("Certificates") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                indicatorColor = selectedIndicatorColor
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painterResource(R.drawable.deployed_code),
                    contentDescription = "Devices"
                )
            },
            selected = navController.currentDestination?.route == "Devices",
            onClick = { navController.navigate("Devices") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                indicatorColor = selectedIndicatorColor
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painterResource(R.drawable.ic_notifications_black_24dp),
                    contentDescription = "Notifications"
                )
            },
            selected = navController.currentDestination?.route == "Notifications",
            onClick = { navController.navigate("Notifications") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                indicatorColor = selectedIndicatorColor
            )
        )
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.person), contentDescription = "Account") },
            selected = navController.currentDestination?.route == "Account",
            onClick = { navController.navigate("Account") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                indicatorColor = selectedIndicatorColor
            )
        )

    }
}