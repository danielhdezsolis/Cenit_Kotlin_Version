package com.example.cenitapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to create DataStore
val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
        val USER_UID_KEY = stringPreferencesKey("userUid")
    }

    // Save token
    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }
    // Guardar el UID en UserPreferences
    suspend fun saveUserUid(userUid: String) {
        dataStore.edit { preferences ->
            preferences[USER_UID_KEY] = userUid
        }
    }

    // Get token
    val accessToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    // Obtener el UID desde UserPreferences
    val userUid: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_UID_KEY]
        }

    // Clear preferences
    suspend fun clearPreferences() {
        dataStore.edit { it.clear() }
    }
}