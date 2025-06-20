package com.example.reshare.domain.usecase.requests

import com.example.reshare.domain.model.Requests
import com.example.reshare.domain.repository.RequestsRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class GetMyRequestsUseCase @Inject constructor(
    private val repository: RequestsRepository
) {
    suspend operator fun invoke(): Resource<List<Requests>> {
        return repository.getMyRequests()
    }
}