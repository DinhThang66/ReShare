package com.example.reshare.presentation.features.mainGraph.home.makeARequest

import com.example.reshare.domain.model.Product

data class MakeRequestState(
    val pickupTime: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val success: Boolean = false,
    val product: Product? = null
)