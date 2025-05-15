package com.example.reshare.presentation.utils

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTimeAgo(isoTime: String): String {
    return try {
        val time = Instant.parse(isoTime)
        val now = Instant.now()
        val duration = Duration.between(time, now)

        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        when {
            minutes < 1 -> "vừa xong"
            minutes == 1L -> "1 phút trước"
            minutes < 60 -> "$minutes phút trước"
            hours == 1L -> "1 giờ trước"
            hours < 24 -> "$hours giờ trước"
            days == 1L -> "1 ngày trước"
            days <= 5 -> "$days ngày trước"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    .withZone(ZoneId.systemDefault())
                formatter.format(time)
            }
        }
    } catch (e: Exception) {
        isoTime // fallback nếu lỗi
    }
}