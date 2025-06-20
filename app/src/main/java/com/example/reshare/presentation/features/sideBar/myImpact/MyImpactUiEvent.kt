package com.example.reshare.presentation.features.sideBar.myImpact

sealed class MyImpactUiEvent {
    data object RefreshMyRequests : MyImpactUiEvent()
    data object RefreshReceivedRequests : MyImpactUiEvent()

    data class UpdateRequestStatus(
        val requestId: String,
        val status: String
    ) : MyImpactUiEvent()
}