package com.example.cenitapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cenitapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomToolbar(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val toolbarTitle = getToolbarTitle(currentBackStackEntry)
    val isDetailsScreen = currentBackStackEntry?.destination?.route == "Details"

    val customFontFamily = FontFamily(
        Font(R.font.spacegrotesk_medium)
    )

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth().then(if (isDetailsScreen) Modifier.padding(end = 40.dp) else Modifier), contentAlignment = Alignment.Center, ) {
                Text(
                    text = toolbarTitle,
                    fontFamily = customFontFamily,
                    fontSize = 22.sp
                )
            }
        },
        navigationIcon = {
            if (isDetailsScreen) {
                BackButton(icon = ImageVector.vectorResource(id = R.drawable.ic_back))  {
                    navController.popBackStack()
                }
            }
        }
    )
}