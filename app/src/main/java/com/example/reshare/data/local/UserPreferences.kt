package com.example.reshare.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val ID = stringPreferencesKey("id")
        val FIRSTNAME = stringPreferencesKey("first_name")
        val LASTNAME = stringPreferencesKey("last_name")
        val EMAIL = stringPreferencesKey("email")
        val PROFILE_PIC = stringPreferencesKey("profile_pic")
        val TOKEN = stringPreferencesKey("token")
        val STREAM_TOKEN = stringPreferencesKey("stream_token")
    }

    val userId: Flow<String?> = context.dataStore.data.map { it[ID] }
    val userFName: Flow<String?> = context.dataStore.data.map { it[FIRSTNAME] }
    val userLName: Flow<String?> = context.dataStore.data.map { it[LASTNAME] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[EMAIL] }
    val userProfilePic: Flow<String?> = context.dataStore.data.map { it[PROFILE_PIC] }
    val userToken: Flow<String?> = context.dataStore.data.map { it[TOKEN] }
    val streamToken: Flow<String?> = context.dataStore.data.map { it[STREAM_TOKEN] }



    suspend fun saveUser(
        id: String,
        firstName: String,
        lastName: String,
        email: String,
        profilePic: String,
        token: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[ID] = id
            prefs[FIRSTNAME] = firstName
            prefs[LASTNAME] = lastName
            prefs[EMAIL] = email
            prefs[PROFILE_PIC] = profilePic
            prefs[TOKEN] = token
        }
    }

    suspend fun saveStreamToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[STREAM_TOKEN] = token
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { it.clear() }
    }
}