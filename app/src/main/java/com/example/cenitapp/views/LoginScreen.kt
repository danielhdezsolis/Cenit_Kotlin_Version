package com.example.cenitapp.views

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cenitapp.R
import com.example.cenitapp.SupabaseAuthViewModel
import com.example.cenitapp.data.model.UserState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cenitapp.components.Alert

@Composable
fun LoginScreen(navController: NavController,viewModel: SupabaseAuthViewModel = viewModel()) {
    val context = LocalContext.current
    val userState by viewModel.userState
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var currentUserState by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) } // Para mostrar el AlertDialog
    var dialogMessage by remember { mutableStateOf("") } // Para el mensaje del AlertDialog
    var passwordVisibility by remember { mutableStateOf(false) }

    val spacegroteskRegular = FontFamily(
        Font(R.font.spacegrotesk_regular)
    )
    val spacegroteskMedium = FontFamily(
        Font(R.font.spacegrotesk_medium)
    )

//    LaunchedEffect(Unit) {
//        viewModel.isUserLoggedIn(
//            context
//        )
//    }

    // Función para validar correo
    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Función para validar contraseña
    fun validatePassword(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(150.dp)) // Ajusta el valor según sea necesario

        Image(
            painter = painterResource(id = R.drawable.cenit_dark_text),
            contentDescription = "Logo",
            modifier = Modifier
                .width(115.dp)
                .height(30.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(50.dp)) // Ajusta el valor según sea necesario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "Correo: ",
                fontFamily = spacegroteskMedium,
                color = colorResource(R.color.text),
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.padding(8.dp))
            TextField(
                value = userEmail,
                placeholder = {
                    Text(
                        "example@correo.com",
                        fontFamily = spacegroteskRegular,
                        color = colorResource(R.color.placeholder),
                        fontSize = 12.sp,
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_at_email),
                        contentDescription = "Email Icon"
                    )
                },
                onValueChange = { userEmail = it },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.inputContainer),
                    unfocusedContainerColor = colorResource(R.color.inputContainer),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            if (emailError != null) {
                Text(text = emailError ?: "", color = Color.Red)
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                "Contraseña: ",
                fontFamily = spacegroteskMedium,
                color = colorResource(R.color.text),
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.padding(8.dp))
            TextField(
                value = userPassword,
                placeholder = {
                    Text(
                        "**********",
                        fontFamily = spacegroteskRegular,
                        color = colorResource(R.color.placeholder),
                        fontSize = 12.sp,
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = "Lock Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        val icon = if (passwordVisibility) {
                            painterResource(R.drawable.ic_visibility_off) // Cambia esto por tu icono de ojo cerrado
                        } else {
                            painterResource(R.drawable.ic_visibility) // Cambia esto por tu icono de ojo abierto
                        }
                        Image(
                            painter = icon,
                            contentDescription = if (passwordVisibility) "Hide Password" else "Show Password"
                        )
                    }
                },
                onValueChange = { userPassword = it },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.inputContainer),
                    unfocusedContainerColor = colorResource(R.color.inputContainer),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            if (passwordError != null) {
                Text(text = passwordError ?: "", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    emailError = if (!validateEmail(userEmail)) {
                        "Correo inválido"
                    } else null
                    passwordError = if (!validatePassword(userPassword)) {
                        "La contraseña debe tener al menos 6 caracteres"
                    } else null

                    if (emailError == null && passwordError == null) {
                        viewModel.login(
                            context,
                            userEmail,
                            userPassword
                        )
                    }
                }) {
                Text(
                    text = "Entrar",
                    fontFamily = spacegroteskRegular,
                    color = colorResource(R.color.white),
                    fontSize = 16.sp
                )
            }
        }

        // Manejo del estado del usuario
        when (userState) {
            is UserState.Loading -> {
                // Muestra un cargando si es necesario
            }

            UserState.Authenticated -> {
                LaunchedEffect(Unit) {
                    navController.navigate("BlankScreen") {
                        popUpTo("Login") { inclusive = true }
                    }
                }
            }

            is UserState.Error -> {
                dialogMessage = "El correo y/o contraseña son incorrectos"
                showDialog = true
            }

            UserState.ClearError -> showDialog = false
            UserState.NotAuthenticated -> {
                // Estado inicial
            }

            UserState.Success -> TODO()
        }

        if (currentUserState.isNotEmpty()) {
            Text(text = currentUserState)
        }
    }

    // AlertDialog para mostrar el error
    if (showDialog) {
        Alert(
            onDismissClick = {
                showDialog = false
                viewModel.resetUserState()
            },
            onConfirmClick = {
                showDialog = false
                viewModel.resetUserState()
            },
            title = "Error de Autenticación",
            text = dialogMessage,
            confirmText = "OK",
            modifier = Modifier.padding(5.dp),
            titleColor = colorResource(R.color.primary),
            textColor = colorResource(R.color.text),
            buttonColor = colorResource(R.color.primary),
            titleFontFamily = spacegroteskRegular, // Usar la fuente personalizada para el título
            titleFontSize = 20f, // Tamaño de fuente personalizado para el título
            textFontFamily = spacegroteskRegular, // Usar la fuente personalizada para el texto
            textFontSize = 14f, // Tamaño de fuente personalizado para el texto
            confirmTextFontSize = 12f // Tamaño de fuente personalizado para el texto del botón
        )
    }



}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen( NavController(LocalContext.current))
}