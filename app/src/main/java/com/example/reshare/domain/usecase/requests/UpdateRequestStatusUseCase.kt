package com.example.reshare.domain.usecase.requests

import com.example.reshare.domain.model.Requests2
import com.example.reshare.domain.repository.RequestsRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class UpdateRequestStatusUseCase @Inject constructor(
    private val repository: RequestsRepository
) {
    suspend operator fun invoke(
        requestId: String,
        status: String
    ): Resource<String> {
        return repository.updateRequestStatus(requestId, status)
    }
}