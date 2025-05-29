package com.example.reshare.presentation.features.mainGraph.explore

import com.example.reshare.domain.model.Product

data class ExploreState (
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)