package com.example.cenitapp

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cenitapp.data.model.RunningProofs
import com.example.cenitapp.data.model.UserState
import com.example.cenitapp.data.network.SupabaseClient.client
import com.example.cenitapp.utils.UserPreferences
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
                    Log.i("Supabase", "No hay token")
                } else {
                    client.gotrue.retrieveUser(token)
                    client.gotrue.refreshCurrentSession()
                    saveToken(userPreferences)
                    _userState.value = UserState.Authenticated
                    Log.i("Supabase", "Hay token")
                }
            } catch (e: Exception) {
                _userState.value = UserState.NotAuthenticated
                Log.i("Supabase", "No hay token: ${e.message}")
            }
        }
    }

    //    // Resetear el estado de error
    fun resetUserState() {
        _userState.value = UserState.ClearError
    }

    //    @Composable
//    fun GetProofs() {
//        var proofs by remember { mutableStateOf<List<RunningProofs>>(listOf()) }
//        LaunchedEffect(Unit) {
//            viewModelScope.launch {
//                try {
//                    proofs = client.postgrest.from("proofs").select(columns = Columns.list("id, created_at, serial_number, status, percentage, result, observations, time")).decodeList<RunningProofs>()
//
//                    Log.d("Supabase", "Proofs: $proofs")
//                } catch (e: Exception) {
//                    Log.e("Supabase", "Error fetching proofs", e)
//                }
//            }
//        }
//    }
//    @Composable
//    fun GetProofs(context: Context) {
//        var proofs by remember { mutableStateOf<List<RunningProofs>>(listOf()) }
//        viewModelScope.launch {
//            try {
//                val userPreferences = UserPreferences(context)
//                // Obtener el token de acceso desde UserPreferences
//                val accessToken = userPreferences.accessToken.first()
//                if (accessToken.isNullOrEmpty()) {
//                    // Obtener la fecha actual para traer solo las running proofs del día de hoy
//                    val today = Calendar.getInstance()
//                    today.set(Calendar.HOUR_OF_DAY, 0)
//                    today.set(Calendar.MINUTE, 0)
//                    today.set(Calendar.SECOND, 0)
//                    today.set(Calendar.MILLISECOND, 0)
//
//                    val startOfToday = today.time
//                    today.set(Calendar.HOUR_OF_DAY, 23)
//                    today.set(Calendar.MINUTE, 59)
//                    today.set(Calendar.SECOND, 59)
//                    today.set(Calendar.MILLISECOND, 999)
//
//                    val endOfToday = today.time
//
//                    // Convertir las fechas a formato ISO 8601
//                    val startOfTodayString =
//                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(
//                            startOfToday
//                        )
//                    val endOfTodayString =
//                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(
//                            endOfToday
//                        )
//
//                    // Realizar la consulta usando el token
//                    proofs = client.postgrest.from("proofs")
//                        .select(
//                            columns = Columns.list("id, created_at, serial_number, status, percentage, result, observations, time"),
//                            filter = {
//                                accessToken?.let { eq("user_id", it) }
//                                isIn("status", listOf("completed", "running", "waiting"))
//                                gte("created_at", startOfTodayString)
//                                lt("created_at", endOfTodayString)
//                                order("id", order = Order.DESCENDING)
//
//                            })
//                        .decodeList<RunningProofs>()           // Decodificar los resultados en la clase RunningProofs
//
//                    Log.d("Supabase", "Proofs: $proofs")
//                } else {
//                    // Manejar el caso donde el token no esté disponible
//                    Log.e("UserPreferences", "Access token is missing.")
//                }
//            } catch (e: Exception) {
//                Log.e("Supabase", "Error fetching proofs", e)
//            }
//
//        }
//    }
    @Composable
    fun GetProofs(context: Context) {
        var proofs by remember { mutableStateOf<List<RunningProofs>>(listOf()) }

        // Ejecutar la operación en una corutina usando LaunchedEffect
        LaunchedEffect(Unit) {
            try {
                val userPreferences = UserPreferences(context)

                // Obtener el token de acceso desde UserPreferences
                val accessToken = userPreferences.accessToken.first()

                if (accessToken.isNullOrEmpty()) {
                    // Obtener la fecha actual para traer solo las running proofs del día de hoy
                    val today = Calendar.getInstance()
                    today.set(Calendar.HOUR_OF_DAY, 0)
                    today.set(Calendar.MINUTE, 0)
                    today.set(Calendar.SECOND, 0)
                    today.set(Calendar.MILLISECOND, 0)

                    val startOfToday = today.time
                    today.set(Calendar.HOUR_OF_DAY, 23)
                    today.set(Calendar.MINUTE, 59)
                    today.set(Calendar.SECOND, 59)
                    today.set(Calendar.MILLISECOND, 999)

                    val endOfToday = today.time

                    // Convertir las fechas a formato ISO 8601
                    val startOfTodayString =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(startOfToday)
                    val endOfTodayString =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(endOfToday)

                    // Realizar la consulta usando el token
                    proofs = client.postgrest.from("proofs")
                        .select(
                            columns = Columns.list("id, created_at, serial_number, status, percentage, result, observations, time"),
                            filter = {
                                accessToken?.let { eq("user_id", it) }
                                isIn("status", listOf("completed", "running", "waiting"))
                                gte("created_at", startOfTodayString)
                                lt("created_at", endOfTodayString)
                                order("id", order = Order.DESCENDING)
                            })
                        .decodeList<RunningProofs>()           // Decodificar los resultados en la clase RunningProofs

                    Log.d("Supabase", "Proofs: $proofs")
                } else {
                    // Manejar el caso donde el token no esté disponible
                    Log.e("UserPreferences", "Access token is missing.")
                }
            } catch (e: Exception) {
                Log.e("Supabase", "Error fetching proofs", e)
            }
        }
    }



}