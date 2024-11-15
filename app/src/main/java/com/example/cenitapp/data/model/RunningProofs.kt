package com.example.cenitapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RunningProofs(
    val id: Int?,
    val created_at: String?,
    val serial_number: String?,
    val status: String?,
    val percentage: Float?,
    val result: Boolean?,
    val observations: String?,
    val time: Int?,
)
