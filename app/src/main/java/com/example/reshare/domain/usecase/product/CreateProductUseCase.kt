package com.example.reshare.domain.usecase.product

import com.example.reshare.domain.repository.ProductRepository
import com.example.reshare.presentation.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>
    ): Resource<Unit> {
        return repository.createProduct(partMap, images)
    }
}