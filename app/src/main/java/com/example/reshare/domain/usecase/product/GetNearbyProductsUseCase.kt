package com.example.reshare.domain.usecase.product

import com.example.reshare.domain.model.Product
import com.example.reshare.domain.repository.ProductRepository
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNearbyProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        forceFetchFromRemote: Boolean,
        query: String? = null
    ): Flow<Resource<List<Product>>> {
        return repository.getNearbyProducts(forceFetchFromRemote, query)
    }
}