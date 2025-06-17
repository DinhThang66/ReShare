package com.example.reshare.presentation.features.sideBar.myListing

import com.example.reshare.domain.model.Product

data class MyListingsState (
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)