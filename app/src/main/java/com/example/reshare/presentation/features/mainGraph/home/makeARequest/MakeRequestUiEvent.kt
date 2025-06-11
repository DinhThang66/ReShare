package com.example.reshare.presentation.features.mainGraph.home.makeARequest

import com.example.reshare.domain.model.Product

sealed class MakeRequestUiEvent {
    data class PickupTimeChanged(val value: String) : MakeRequestUiEvent()
    data class MessageChanged(val value: String) : MakeRequestUiEvent()
    data class SubmitRequest(val productId: String) : MakeRequestUiEvent()

    data class SetProduct(val product: Product) : MakeRequestUiEvent()
}