package com.example.cenitapp.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.example.cenitapp.R

@Composable
fun getToolbarTitle(backStackEntry: NavBackStackEntry?): String {
    return when (backStackEntry?.destination?.route) {
        "RunningProofs" -> stringResource(R.string.RunningProofs_Header)
        "Onboarding" -> stringResource(R.string.OnBoarding_Header)
        "Certificates" -> stringResource(R.string.Certificates_Header)
        "Devices" -> stringResource(R.string.Devices_Header)
        "Notifications" -> stringResource(R.string.Notifications_Header)
        "Account" -> stringResource(R.string.Account_Header)
        "Details" -> stringResource(R.string.Details_Header)
        else -> stringResource(R.string.Default_Header)
    }
}