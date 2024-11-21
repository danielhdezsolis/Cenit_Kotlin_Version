package com.example.cenitapp.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cenitapp.data.model.RunningProofs
import com.example.cenitapp.data.network.SupabaseClient.client
import com.example.cenitapp.utils.UserPreferences
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RunningProofsViewModel : ViewModel() {
    val proofs = mutableStateOf<List<RunningProofs>>(emptyList())
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    @SuppressLint("NewApi")
    fun fetchProofs(context: Context) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val userPreferences = UserPreferences(context)
                val accessToken = userPreferences.accessToken.first()
                Log.d("Supabase", "Access Token: $accessToken")

                if (accessToken.isNullOrEmpty()) {
                    errorMessage.value = "Access token is missing."
                    Log.e("RunningProofsViewModel", "Access token is null or empty.")
                    return@launch
                }
                // Obtener el userUid desde las preferencias
                val userUid = userPreferences.userUid.first()
                Log.d("Supabase", "User UID: $userUid")

                if (userUid.isNullOrEmpty()) {
                    errorMessage.value = "User UID is missing."
                    Log.e("RunningProofsViewModel", "User UID is null or empty.")
                    return@launch
                }

                // Obtener la fecha de hoy
                val today = LocalDate.now()

                // Definir el inicio y fin del día
                val startOfDayLocal = LocalDateTime.of(today, LocalTime.MIDNIGHT)
                val endOfDayLocal = LocalDateTime.of(today, LocalTime.MAX)

                // Convertir las fechas a Instant en UTC
                val startOfDayUTC = startOfDayLocal.atZone(ZoneOffset.systemDefault()).toInstant()
                val endOfDayUTC = endOfDayLocal.atZone(ZoneOffset.systemDefault()).toInstant()

                // Ajuste de las fechas considerando la diferencia con UTC
                val startOfDayDate = Date.from(startOfDayUTC)
                val endOfDayDate = Date.from(endOfDayUTC)

                // Formatear a UTC
                val sdfUTC =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }

                val startOfTodayUTC = sdfUTC.format(startOfDayDate)
                val endOfTodayUTC = sdfUTC.format(endOfDayDate)

                Log.d("Supabase", "Hoy es: $today")
                Log.d("Supabase", "Definir inicio: $startOfDayLocal")
                Log.d("Supabase", "Definir fin: $endOfDayLocal")
                Log.d("Supabase", "Fecha inicio to UTC: $startOfDayUTC")
                Log.d("Supabase", "Fecha fin to UTC: $endOfDayUTC")
                Log.d("Supabase", "Inicio con ajuste con diferencia UTC: $startOfDayDate")
                Log.d("Supabase", "Fin con ajuste con diferencia UTC: $endOfDayDate")
                Log.d("Supabase", "Start of Today (UTC): $startOfTodayUTC")
                Log.d("Supabase", "End of Today (UTC): $endOfTodayUTC")
                delay(500)
                // Ejecutar consulta
                proofs.value = client.postgrest.from("proofs")
                    .select(
                        columns = Columns.list("id, created_at, serial_number, status, percentage, result, observations, time")
                    ) {
                        filter {
                            eq("user_id", userUid)
                            isIn("status", listOf("completed", "running", "waiting"))
                            and {
                                gte("created_at", startOfTodayUTC)
                                lte("created_at", endOfTodayUTC)
                            }
                        }
                        order("id", order = Order.DESCENDING)
                    }
                    .decodeList()
            } catch (e: Exception) {
                errorMessage.value = "Error fetching proofs: ${e.message}"
                Log.e("RunningProofsViewModel", "Error fetching proofs", e)
            } finally {
                isLoading.value = false
            }
        }
    }

}