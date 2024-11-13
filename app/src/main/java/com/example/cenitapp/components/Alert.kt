package com.example.cenitapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Alert(
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
    title: String,
    text: String,
    confirmText: String,
    modifier: Modifier = Modifier, // Permite personalizar el diseño con un Modifier
    titleColor: Color = Color.Black, // Color personalizable para el título
    textColor: Color = Color.Black, // Color personalizable para el texto
    buttonColor: Color = Color.Blue, // Color personalizable para el botón de confirmación
    titleFontFamily: FontFamily = FontFamily.Default, // FontFamily personalizable para el título
    titleFontSize: Float = 20f, // FontSize personalizable para el título
    textFontFamily: FontFamily = FontFamily.Default, // FontFamily personalizable para el texto
    textFontSize: Float = 16f, // FontSize personalizable para el texto
    confirmTextFontSize: Float = 16f // FontSize personalizado para el texto del botón
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    fontFamily = titleFontFamily, // Usamos el FontFamily personalizado
                    fontSize = titleFontSize.sp, // Usamos el FontSize personalizado
                    modifier = Modifier.padding(bottom = 8.dp) // Agregamos un pequeño espacio debajo
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    color = textColor,
                    fontFamily = textFontFamily, // Usamos el FontFamily personalizado
                    fontSize = textFontSize.sp, // Usamos el FontSize personalizado
                    modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmClick,
                modifier = modifier,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text(
                    text = confirmText,
                    fontFamily = textFontFamily, // Usamos el FontFamily personalizado para el texto del botón
                    fontSize = confirmTextFontSize.sp // Usamos el FontSize personalizado para el texto del botón
                )
            }
        }
    )
}
