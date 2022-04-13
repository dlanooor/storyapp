package com.example.storyapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSession private constructor(private val dataStore: DataStore<Preferences>) {

    private val THEME_KEY = stringPreferencesKey("user_token")

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserSession? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserSession {
            return INSTANCE ?: synchronized(this) {
                val instance = UserSession(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}