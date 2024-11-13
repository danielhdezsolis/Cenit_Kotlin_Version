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
    }

    // Save token
    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    // Get token
    val accessToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }

    // Clear preferences
    suspend fun clearPreferences() {
        dataStore.edit { it.clear() }
    }
}