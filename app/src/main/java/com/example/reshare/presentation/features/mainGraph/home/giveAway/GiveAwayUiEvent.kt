package com.example.reshare.presentation.features.mainGraph.home.giveAway

import android.net.Uri

sealed class GiveAwayUiEvent {
    data class AddImages(val images: List<Uri>) : GiveAwayUiEvent()
    data class RemoveImage(val uri: Uri) : GiveAwayUiEvent()

    data class SetTitle(val title: String) : GiveAwayUiEvent()
    data class SetDescription(val description: String) : GiveAwayUiEvent()
    data class SetPickupTime(val pickupTime: String) : GiveAwayUiEvent()
    data class SetInstructions(val instructions: String) : GiveAwayUiEvent()
    data class SetPostType(val type: String) : GiveAwayUiEvent()
    data class SetProductType(val type: String) : GiveAwayUiEvent()
}