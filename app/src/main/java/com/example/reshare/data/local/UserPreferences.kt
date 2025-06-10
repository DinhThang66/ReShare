package com.example.reshare.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
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
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
        val HAS_LOCATION = booleanPreferencesKey("hasLocation")

        val TOKEN = stringPreferencesKey("token")
        val STREAM_TOKEN = stringPreferencesKey("stream_token")
    }

    val userId: Flow<String?> = context.dataStore.data.map { it[ID] }
    val userFName: Flow<String?> = context.dataStore.data.map { it[FIRSTNAME] }
    private val userLName: Flow<String?> = context.dataStore.data.map { it[LASTNAME] }
    private val userEmail: Flow<String?> = context.dataStore.data.map { it[EMAIL] }
    val userProfilePic: Flow<String?> = context.dataStore.data.map { it[PROFILE_PIC] }

    val latitude: Flow<Double?> = context.dataStore.data.map { it[LATITUDE] }
    val longitude: Flow<Double?> = context.dataStore.data.map { it[LONGITUDE] }
    val hasLocation: Flow<Boolean> = context.dataStore.data.map { it[HAS_LOCATION] ?: false }

    val userToken: Flow<String?> = context.dataStore.data.map { it[TOKEN] }
    val streamToken: Flow<String?> = context.dataStore.data.map { it[STREAM_TOKEN] }

    suspend fun saveUser(
        id: String,
        firstName: String,
        lastName: String,
        email: String,
        profilePic: String,
        latitude: Double?,
        longitude: Double?,
        hasLocation: Boolean,
        token: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[ID] = id
            prefs[FIRSTNAME] = firstName
            prefs[LASTNAME] = lastName
            prefs[EMAIL] = email
            prefs[PROFILE_PIC] = profilePic

            latitude?.let { prefs[LATITUDE] = it } ?: prefs.remove(LATITUDE)
            longitude?.let { prefs[LONGITUDE] = it } ?: prefs.remove(LONGITUDE)
            prefs[HAS_LOCATION] = hasLocation
            prefs[TOKEN] = token
        }
    }
    suspend fun saveHasLocation(
        hasLocation: Boolean,
        latitude: Double,
        longitude: Double,
    ) {
        context.dataStore.edit { prefs ->
            prefs[HAS_LOCATION] = hasLocation
            prefs[LATITUDE] = latitude
            prefs[LONGITUDE] = longitude
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
            userId, userFName, userLName, userEmail, userProfilePic,
            latitude, longitude,
            userToken
        ) { values: Array<Any?> ->
            val id = values[0] as? String
            val firstName = values[1] as? String
            val lastName = values[2] as? String
            val email = values[3] as? String
            val profilePic = values[4] as? String
            val latitude = values[5] as? Double?
            val longitude = values[6] as? Double?
            val token = values[7] as? String
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
                    latitude = latitude,
                    longitude = longitude
                )
            } else null
        }
    }
}