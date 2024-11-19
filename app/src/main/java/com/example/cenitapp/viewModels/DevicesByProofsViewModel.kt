//package com.example.cenitapp.viewModels
//
//import android.content.Context
//import android.util.Log
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.cenitapp.data.model.DevicesByProofs
//import com.example.cenitapp.data.model.ElectricData
//import com.example.cenitapp.data.model.EnvironmentData
//import com.example.cenitapp.data.model.LastData
//import com.example.cenitapp.data.network.SupabaseClient.client
//import com.example.cenitapp.utils.UserPreferences
//import io.github.jan.supabase.postgrest.postgrest
//import io.github.jan.supabase.postgrest.query.Columns
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.launch
//import java.lang.Exception
//
//class DevicesByProofsViewModel : ViewModel() {
//    val devices = mutableStateOf<List<DevicesByProofs>>(emptyList())
//    val isLoading = mutableStateOf(false)
//    val errorMessage = mutableStateOf<String?>(null)
//
//    fun fetchDevicesByProofs(context: Context, ids: List<Int>) {
//        isLoading.value = true
//        errorMessage.value = null
//
//        viewModelScope.launch {
//            try {
//                val userPreferences = UserPreferences(context)
//                val accessToken = userPreferences.accessToken.first()
//
//                if (accessToken.isNullOrEmpty()) {
//                    errorMessage.value = "Access token is missing."
//                    Log.e("DevicesByProofsViewModel", "Access token is null or empty.")
//                    return@launch
//                }
//
//                // Realizar consulta a Supabase para obtener los dispositivos asociados a las pruebas
//                val response = client.postgrest.from("proofs_devices")
//                    .select(Columns.list("id, id_proof, id_device, last_data, devices(serial_number)")){
//                        isIn("id_proof", ids)
//                        eq("user_id", accessToken) // Asegúrate de que 'userId' esté disponible
//                    }
//
//                if (response.error != null) {
//                    errorMessage.value = response.error.message
//                    Log.e("DevicesByProofsViewModel", "Error fetching devices: ${response.error.message}")
//                } else {
//                    // Procesar datos obtenidos
//                    val deviceData = response.data.map { item ->
//                        val lastData = item["last_data"] as Map<String, Any>
//                        val lastDataObject = when {
//                            lastData.containsKey("energy") -> LastData.Electric(
//                                ElectricData(
//                                    energy = lastData["energy"] as Float,
//                                    voltage = lastData["voltage"] as Float,
//                                    current = lastData["current"] as Float,
//                                    power = lastData["power"] as Float
//                                )
//                            )
//                            lastData.containsKey("temperature") -> LastData.Environment(
//                                EnvironmentData(
//                                    temperature = lastData["temperature"] as Float,
//                                    humidity = lastData["humidity"] as Float,
//                                    battery = lastData["battery"] as Float
//                                )
//                            )
//                            else -> null
//                        }
//
//                        DevicesByProofs(
//                            id = item["id"] as Int,
//                            id_proof = item["id_proof"] as Int,
//                            id_device = item["id_device"] as Int,
//                            serial_number = item["devices"]?.get("serial_number") as String,
//                            last_data = lastDataObject!!
//                        )
//                    }
//
//                    devices.value = deviceData
//                }
//            } catch (e: Exception) {
//                errorMessage.value = "Error fetching devices: ${e.message}"
//                Log.e("DevicesByProofsViewModel", "Error fetching devices", e)
//            } finally {
//                isLoading.value = false
//            }
//        }
//    }
//}
//
