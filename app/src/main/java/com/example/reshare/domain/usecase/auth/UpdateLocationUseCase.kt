package com.example.reshare.domain.usecase.auth

import com.example.reshare.domain.repository.AuthRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class UpdateLocationUseCase  @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Resource<String> {
        return repository.updateLocation(latitude, longitude)
    }
}