package com.example.cenitapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ElectricData(
    val energy: Float,
    val voltage: Float,
    val current: Float,
    val power: Float
)

@Serializable
data class EnvironmentData(
    val temperature: Float,
    val humidity: Float,
    val battery: Float
)

@Serializable
data class RunningData(
    val id: Int,
    val id_proof: Int,
    val id_device: Int,
    val serial_number: String,
    val last_data: LastData // Puede ser ElectricData o EnvironmentData
)

@Serializable
data class DevicesByProofs(
    val id: Int,
    val id_proof: Int,
    val id_device: Int,
    val serial_number: String,
    val last_data: LastData // ElectricData o EnvironmentData
)

@Serializable
sealed class LastData {
    @Serializable
    data class Electric(val electricData: ElectricData) : LastData()

    @Serializable
    data class Environment(val environmentData: EnvironmentData) : LastData()
}

