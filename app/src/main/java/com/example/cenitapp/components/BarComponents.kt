package com.example.cenitapp.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cenitapp.R

@Composable
fun TitleBar(name: String) {
    val spacegroteskMedium = FontFamily(
        Font(R.font.spacegrotesk_medium)
    )
    Text(
        text = name,
        fontFamily = spacegroteskMedium,
        fontSize = 22.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun FAB(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("Details") },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        shape = ButtonDefaults.shape,
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null
        )
    }
}