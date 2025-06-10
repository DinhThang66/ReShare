package com.example.reshare.presentation.utils

import androidx.compose.ui.graphics.Color
import com.example.reshare.data.remote.dto.GeocodingResult
import com.example.reshare.ui.theme.BlueD
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.LightPurple
import com.example.reshare.ui.theme.MilkM
import com.example.reshare.ui.theme.OrangeM
import com.example.reshare.ui.theme.YellowD
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

fun String.capitalizeFirst(): String =
    this.replaceFirstChar { it.uppercase() }

data class BadgeStyle(
    val backgroundColor: Color,
    val textColor: Color
)
object BadgeStyles {
    val Free = BadgeStyle(LightPurple, DarkPurple)
    val Wanted = BadgeStyle(MilkM, OrangeM)
    val Paid = BadgeStyle(Color.Black.copy(0.6f), Color.White)
    val Reduced = BadgeStyle(YellowD, BlueD)
}

fun getBadgeStyle(tag: String): BadgeStyle {
    return when (tag.lowercase()) {
        "free" -> BadgeStyles.Free
        "wanted" -> BadgeStyles.Wanted
        "paid" -> BadgeStyles.Paid
        "reduced" -> BadgeStyles.Reduced
        else -> BadgeStyles.Free
    }
}

fun getStreetOrDistrict(result: GeocodingResult): String? {
    val components = result.address_components

    val street = components.firstOrNull { "route" in it.types }?.long_name
    if (!street.isNullOrEmpty()) return street

    val district = components.firstOrNull { "administrative_area_level_2" in it.types }?.long_name
    return district
}