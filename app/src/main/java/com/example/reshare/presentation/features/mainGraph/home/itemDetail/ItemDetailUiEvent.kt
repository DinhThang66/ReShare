package com.example.reshare.presentation.features.mainGraph.home.itemDetail

import com.example.reshare.domain.model.Product

sealed class ItemDetailUiEvent {
    data class SetProduct(val product: Product) : ItemDetailUiEvent()
}