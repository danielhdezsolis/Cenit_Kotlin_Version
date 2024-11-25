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
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
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
                    .decodeList<RunningProofs>()
            } catch (e: Exception) {
                errorMessage.value = "Error fetching proofs: ${e.message}"
                Log.e("RunningProofsViewModel", "Error fetching proofs", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    @OptIn(SupabaseExperimental::class)
    fun subscribeToProofsEvents(scope: CoroutineScope, context: Context) {
        viewModelScope.launch {
            try {
                // Obtener el userUid desde UserPreferences
                val userPreferences = UserPreferences(context)
                val userUid = userPreferences.userUid.first()
                val channel = client.realtime.channel("proofs")
                val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "proofs"
                    if (userUid != null) {
                        filter("user_id", FilterOperator.EQ, userUid)
                    }
                }

                changeFlow.onEach { postgresAction ->
                    when (postgresAction) {
                        is PostgresAction.Delete -> {
                            Log.d("ProofsEvent", "Deleted: ${postgresAction.oldRecord}")
                            updateProofsOnDelete(postgresAction.oldRecord)
                        }

                        is PostgresAction.Insert -> {
                            Log.d("ProofsEvent", "Inserted: ${postgresAction.record}")
                            updateProofsOnInsert(postgresAction.record)
                        }

                        is PostgresAction.Update -> {
                            Log.d(
                                "ProofsEvent",
                                "Updated: ${postgresAction.oldRecord} with ${postgresAction.record}"
                            )
                            updateProofsOnUpdate(postgresAction.record)
                        }

                        else -> {
                            Log.d(
                                "ProofsEvent",
                                "Unhandled action: ${postgresAction.javaClass.simpleName}"
                            )
                        }
                    }
                }.launchIn(scope)

                channel.subscribe()
            } catch (e: Exception) {
                Log.e("RunningProofsViewModel", "Error subscribing to realtime updates", e)
            }
        }
    }

    private fun updateProofsOnDelete(deletedProof: Map<String, Any>?) {
        if (deletedProof != null) {
            val deletedId = deletedProof["id"]?.toString()?.toInt() ?: return

            // Filtrar la lista para eliminar el registro con el ID eliminado
            proofs.value = proofs.value.filterNot { it.id == deletedId }
        }
    }

    private fun updateProofsOnInsert(newProof: Map<String, Any>?) {
        val newProof = parseProof(newProof) ?: return
        val currentProofs = proofs.value.toMutableList()

        // Añadir el nuevo registro a la lista
        proofs.value = listOf(newProof) + currentProofs
//        if (newProof != null) {
//            val proof = RunningProofs(
//                id = newProof["id"]?.toString()?.toInt()
//                    ?: return, // Si el ID es nulo o no válido, no procedemos
//                created_at = newProof["created_at"]?.toString(),
//                serial_number = newProof["serial_number"]?.toString(),
//                status = newProof["status"]?.toString(),
//                percentage = (newProof["percentage"] as? Number)?.toFloat(),
//                result = newProof["result"] as? Boolean,
//                observations = newProof["observations"]?.toString(),
//                time = (newProof["time"] as? Number)?.toInt()
//            )
//            Log.d("ProofsEvent", "Updated proofs list after insert: ${proofs.value}")
//            proofs.value = listOf(proof) + proofs.value
//        }
    }

    private fun updateProofsOnUpdate(updatedProof: Map<String, Any>?) {
        val updatedProof = parseProof(updatedProof) ?: return
        val currentProofs = proofs.value.toMutableList()
        val updatedIndex = currentProofs.indexOfFirst { it.id == updatedProof.id }

        if (updatedProof.status in listOf("closed", "canceled", "completed-manual")) {
            // Si el estado es 'closed', 'canceled' o 'completed-manual', eliminamos el registro
            proofs.value = currentProofs.filterNot { it.id == updatedProof.id }
        } else if (updatedIndex != -1) {
            // Si el registro existe, actualizamos sus datos
            currentProofs[updatedIndex] = updatedProof
            proofs.value = currentProofs
        } else {
            // Si el registro no existe, lo añadimos a la lista
            proofs.value = listOf(updatedProof) + currentProofs
        }
//        if (updatedProof != null) {
//            val updatedId = updatedProof["id"]?.toString()?.toInt()
//            Log.d("ProofsEvent", "ID update: $updatedId")
//
//            // Si el id es válido, intentamos actualizar
//            if (updatedId != null) {
//                proofs.value = proofs.value.map { proof ->
//                    if (proof.id == updatedId) {
//                        // Creamos una nueva instancia con los valores actualizados
//                        proof.copy(
//                            id = updatedProof["id"]?.toString()?.toInt() ?: proof.id,
//                            created_at = updatedProof["created_at"]?.toString() ?: proof.created_at,
//                            serial_number = updatedProof["serial_number"]?.toString()
//                                ?: proof.serial_number,
//                            status = updatedProof["status"]?.toString() ?: proof.status,
//                            percentage = (updatedProof["percentage"] as? Number)?.toFloat()
//                                ?: proof.percentage,
//                            result = updatedProof["result"] as? Boolean ?: proof.result,
//                            observations = updatedProof["observations"]?.toString()
//                                ?: proof.observations,
//                            time = (updatedProof["time"] as? Number)?.toInt() ?: proof.time
//                        )
//                    } else {
//                        proof // Retornamos la prueba sin cambios si el id no coincide
//                    }
//                }
//            } else {
//                Log.e("ProofsEvent", "Invalid or missing proof ID in the update event")
//            }
//        } else {
//            Log.e("ProofsEvent", "Updated proof data is null")
//        }
    }

    private fun parseProof(record: Map<String, Any>?): RunningProofs? {
        return record?.let {
            RunningProofs(
                id = it["id"]?.toString()?.toInt() ?: return null,
                created_at = it["created_at"]?.toString(),
                serial_number = it["serial_number"]?.toString(),
                status = it["status"]?.toString(),
                percentage = (it["percentage"] as? Number)?.toFloat(),
                result = it["result"] as? Boolean,
                observations = it["observations"]?.toString(),
                time = (it["time"] as? Number)?.toInt()
            )
        }
    }
}