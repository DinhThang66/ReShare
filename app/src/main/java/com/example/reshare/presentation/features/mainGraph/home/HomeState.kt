package com.example.reshare.presentation.features.mainGraph.home

import com.example.reshare.domain.model.CategorizedProducts
import com.example.reshare.domain.model.Product

data class HomeState(
    val isLoading: Boolean = false,
    val freeFood: List<Product> = emptyList(),
    val nonFood: List<Product> = emptyList(),
    val reducedFood: List<Product> = emptyList(),
    val want: List<Product> = emptyList(),
    val error: String = "",

    val userLocation: String = ""
)