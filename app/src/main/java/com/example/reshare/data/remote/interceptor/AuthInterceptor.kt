package com.example.reshare.data.remote.interceptor

import android.util.Log
import com.example.reshare.data.local.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val userPreferences: UserPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = runBlocking {
            userPreferences.userToken.firstOrNull()
        }
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Cookie", "jwt=$token")
            Log.d("jwt", token)
        } else {
            Log.d("jwt", "No token found")
        }

        return chain.proceed(requestBuilder.build())
    }
}