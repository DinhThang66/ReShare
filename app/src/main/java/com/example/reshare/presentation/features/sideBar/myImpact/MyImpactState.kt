package com.example.reshare.presentation.features.sideBar.myImpact

import com.example.reshare.domain.model.Product
import com.example.reshare.domain.model.Requests
import com.example.reshare.domain.model.Requests2

data class MyImpactState (
    val myRequests: List<Requests> = emptyList(),
    val isMyRequestsLoading: Boolean = false,
    val errorMyRequests: String = "",

    val initialRefresh: Boolean = false,
    val receivedRequests: List<Requests2> = emptyList(),
    val isReceivedRequestsLoading: Boolean = false,
    val errorReceivedRequests: String = "",

    val isUpdateStatus: Boolean = false
)