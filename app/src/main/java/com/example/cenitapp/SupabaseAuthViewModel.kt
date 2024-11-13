package com.example.cenitapp

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cenitapp.data.model.UserState
import com.example.cenitapp.data.network.SupabaseClient.client
import com.example.cenitapp.utils.UserPreferences
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SupabaseAuthViewModel : ViewModel() {

    private val _userState = mutableStateOf<UserState>(UserState.Loading)
    val userState: State<UserState> = _userState

    // Validación de correo y contraseña
    private fun validateEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun validatePassword(password: String) = password.length >= 6

    private fun showValidationError() {
        _userState.value = UserState.Error
    }

    // Registro de usuario con validaciones
    fun signUp(context: Context, userEmail: String, userPassword: String) {
        val userPreferences = UserPreferences(context)
        viewModelScope.launch {
            if (!validateEmail(userEmail)) {
                showValidationError()
                return@launch
            }
            if (!validatePassword(userPassword)) {
                showValidationError()
                return@launch
            }

            _userState.value = UserState.Loading
            try {
                client.gotrue.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveToken(userPreferences)
                _userState.value = UserState.Success
            } catch (e: Exception) {
                _userState.value = UserState.Error
            }
        }
    }

    // Guardar token usando DataStore
    private suspend fun saveToken(userPreferences: UserPreferences) {
        val accessToken = client.gotrue.currentAccessTokenOrNull() ?: ""
        userPreferences.saveAccessToken(accessToken)
    }

    // Inicio de sesión con validaciones
    fun login(context: Context, userEmail: String, userPassword: String) {
        val userPreferences = UserPreferences(context)
        viewModelScope.launch {
            if (!validateEmail(userEmail)) {
                showValidationError()
                return@launch
            }
            if (!validatePassword(userPassword)) {
                showValidationError()
                return@launch
            }

            _userState.value = UserState.Loading
            try {
                client.gotrue.loginWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveToken(userPreferences)
                _userState.value = UserState.Authenticated
            } catch (e: Exception) {
                _userState.value = UserState.Error
            }
        }
    }

    // Cierre de sesión y limpieza de preferencias
    fun logout(context: Context) {
        val userPreferences = UserPreferences(context)
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.gotrue.logout()
                userPreferences.clearPreferences()
                _userState.value = UserState.NotAuthenticated
            } catch (e: Exception) {
                _userState.value = UserState.Error
            }
        }
    }

    // Comprobar si el usuario está autenticado
    fun isUserLoggedIn(context: Context) {
        val userPreferences = UserPreferences(context)
        viewModelScope.launch {
            try {
                val token = userPreferences.accessToken.first()
                if (token.isNullOrEmpty()) {
                    _userState.value = UserState.NotAuthenticated
                } else {
                    client.gotrue.retrieveUser(token)
                    client.gotrue.refreshCurrentSession()
                    saveToken(userPreferences)
                    _userState.value = UserState.Authenticated
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error
            }
        }
    }

    // Resetear el estado de error
    fun resetUserState() {
        _userState.value = UserState.ClearError
    }
}