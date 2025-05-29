package com.example.reshare.domain.usecase.product

import com.example.reshare.domain.model.CategorizedProducts
import com.example.reshare.domain.repository.ProductRepository
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategorizedProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        forceFetchFromRemote: Boolean
    ): Flow<Resource<CategorizedProducts>> {
        return repository.getCategorizedProducts(forceFetchFromRemote)
    }
}