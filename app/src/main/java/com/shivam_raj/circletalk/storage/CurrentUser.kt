package com.shivam_raj.circletalk.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CurrentUserManager {
    private const val DATA_STORE_NAME = "User"
    private val USER_ID_KEY = stringPreferencesKey("user_id")

    val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    suspend fun storeCurrentUserId(context: Context, userId: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    fun getCurrentUserId(context: Context): Flow<String?> {
        return context.userPreferencesDataStore.data
            .map { preferences ->
                preferences[USER_ID_KEY]
            }
    }

    suspend fun clearCurrentUserId(context: Context) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }
}