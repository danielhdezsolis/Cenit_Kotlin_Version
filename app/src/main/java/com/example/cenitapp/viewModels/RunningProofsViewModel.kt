package com.example.cenitapp.viewModels

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
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class RunningProofsViewModel: ViewModel() {
    val proofs = mutableStateOf<List<RunningProofs>>(emptyList())
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

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

            // Configuración del inicio del día (local)
            val startOfToday = Calendar.getInstance(TimeZone.getDefault()).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Configuración del fin del día (local)
            val endOfToday = Calendar.getInstance(TimeZone.getDefault()).apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }

            // Formatear a UTC
            val sdfUTC = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

            val startOfTodayUTC = sdfUTC.format(startOfToday.time)+ "+00:00"
            val endOfTodayUTC = sdfUTC.format(endOfToday.time)+ "+00:00"

            Log.d("Supabase", "Start of Today (Local): ${startOfToday.time}")
            Log.d("Supabase", "End of Today (Local): ${endOfToday.time}")
            Log.d("Supabase", "Start of Today (UTC): $startOfTodayUTC")
            Log.d("Supabase", "End of Today (UTC): $endOfTodayUTC")
            delay(500)
            // Ejecutar consulta
            proofs.value = client.postgrest.from("proofs")
                .select(
                    columns = Columns.list("id, created_at, serial_number, status, percentage, result, observations, time")
                ) {
                    eq("user_id", userUid)
                    isIn("status", listOf("completed", "running", "waiting"))
                    gte("created_at", startOfTodayUTC)
                    lte("created_at", endOfTodayUTC)
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