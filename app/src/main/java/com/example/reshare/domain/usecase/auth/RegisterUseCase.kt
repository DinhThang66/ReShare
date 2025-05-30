package com.example.reshare.domain.usecase.auth

import com.example.reshare.domain.model.AuthResult
import com.example.reshare.domain.repository.AuthRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Resource<AuthResult> {
        return repository.register(firstName, lastName, email, password)
    }
}