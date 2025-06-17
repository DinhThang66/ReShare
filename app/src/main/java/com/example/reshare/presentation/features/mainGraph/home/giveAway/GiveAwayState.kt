package com.example.reshare.presentation.features.mainGraph.home.giveAway

import android.net.Uri

data class GiveAwayState (
    val selectedImages: List<Uri> = emptyList(),

    val title: String = "",
    val description: String = "",
    val pickupTime: String = "",
    val instructions: String = "",
    val postType: String = "free",
    val productType: String = "food"
)