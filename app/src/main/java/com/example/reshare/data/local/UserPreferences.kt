package com.example.reshare.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.reshare.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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

    fun getUserFlow(): Flow<User?> {
        return combine(
            userId,
            userFName,
            userLName,
            userEmail,
            userProfilePic,
            userToken
        ) { values: Array<Any?> ->
            val id = values[0] as? String
            val firstName = values[1] as? String
            val lastName = values[2] as? String
            val email = values[3] as? String
            val profilePic = values[4] as? String
            val token = values[5] as? String
            if (
                id != null &&
                firstName != null &&
                lastName != null &&
                email != null &&
                profilePic != null &&
                token != null
            ) {
                User(
                    id = id,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    profilePic = profilePic,
                )
            } else null
        }
    }
}