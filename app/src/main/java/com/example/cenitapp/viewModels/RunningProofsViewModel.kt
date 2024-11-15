package com.example.cenitapp.viewModels

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cenitapp.data.model.RunningProofs
import com.example.cenitapp.data.network.SupabaseClient.client
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

class RunningProofsViewModel: ViewModel() {
    @Composable
    fun GetProofs() {
        var proofs by remember { mutableStateOf<List<RunningProofs>>(listOf()) }

        LaunchedEffect(Unit) {
            viewModelScope.launch {
                try {
                    proofs = client.postgrest.from("proofs").select(columns = Columns.list("id, created_at, serial_number, status, percentage, result, observations, time")).decodeList<RunningProofs>()

                    Log.d("Supabase", "Proofs: $proofs")
                } catch (e: Exception) {
                    Log.e("Supabase", "Error fetching proofs", e)
                }
            }
        }
    }
}