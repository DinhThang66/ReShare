package com.example.reshare.domain.repository

import com.example.reshare.domain.model.CategorizedProducts
import com.example.reshare.domain.model.Product
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getCategorizedProducts(
        forceFetchFromRemote: Boolean
    ): Flow<Resource<CategorizedProducts>>

    suspend fun getNearbyProducts(
        forceFetchFromRemote: Boolean,
        query: String? = null
    ): Flow<Resource<List<Product>>>

    suspend fun getMyProducts(
        forceFetchFromRemote: Boolean,
    ): Flow<Resource<List<Product>>>
}