package com.example.reshare.domain.usecase.requests

import com.example.reshare.domain.model.User
import com.example.reshare.domain.repository.RequestsRepository
import com.example.reshare.domain.repository.UserRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class SendProductRequestUseCase @Inject constructor(
    private val repository: RequestsRepository
) {
    suspend operator fun invoke(
        productId: String,
        pickupTime: String,
        message: String?,
    ): Resource<String> {
        return repository.sendProductRequest(productId, pickupTime, message)
    }
}
