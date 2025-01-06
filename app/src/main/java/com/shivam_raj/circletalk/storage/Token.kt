package com.shivam_raj.circletalk.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object FCMTokenManager {
    private const val DATA_STORE_NAME = "Token"
    private val TOKEN_ID_KEY = stringPreferencesKey("token")
    private val TARGET_ID_KEY = stringPreferencesKey("target_id")

    val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)


    suspend fun storeTokenDetail(context: Context, target: String, token: String){
        storeTarget(context, target)
        storeToken(context, token)
    }

    suspend fun storeToken(context: Context, token: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[TOKEN_ID_KEY] = token
        }
    }

    suspend fun storeTarget(context: Context, target: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[TARGET_ID_KEY] = target
        }
    }


    fun getToken(context: Context): Flow<String?> {
        return context.userPreferencesDataStore.data
            .map { preferences ->
                preferences[TOKEN_ID_KEY]
            }
    }

    fun getTargetId(context: Context): Flow<String?> {
        return context.userPreferencesDataStore.data
            .map { preferences ->
                preferences[TARGET_ID_KEY]
            }
    }
}