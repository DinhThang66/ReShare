package com.example.reshare.domain.usecase.user

import com.example.reshare.domain.model.User
import com.example.reshare.domain.repository.UserRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): Resource<User> {
        return repository.getUser(userId)
    }
}