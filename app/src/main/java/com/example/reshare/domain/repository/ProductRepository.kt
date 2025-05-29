package com.example.reshare.domain.repository

import com.example.reshare.domain.model.CategorizedProducts
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getCategorizedProducts(
        forceFetchFromRemote: Boolean
    ): Flow<Resource<CategorizedProducts>>
}