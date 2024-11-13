package com.example.cenitapp.utils

import android.content.Context

class SharePreferenceHelper(private val context: Context) {
    companion object {
        private const val MY_PREF_KEY = "MY_PREF"
    }

    fun saveString(key: String, data: String) {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, data).apply()
    }

    fun getStringData(key: String): String? {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }

    fun cleaPreferences() {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

}