package com.example.reshare.presentation.utils

fun isTokenExpired(token: String): Boolean {
    return try {
        val parts = token.split(".")
        if (parts.size != 3) return true

        val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
        val regex = Regex("\"exp\":(\\d+)")
        val matchResult = regex.find(payload)
        val exp = matchResult?.groupValues?.get(1)?.toLongOrNull() ?: return true

        val currentTime = System.currentTimeMillis() / 1000
        exp < currentTime
    } catch (e: Exception) {
        true // lỗi coi như token hết hạn
    }
}
