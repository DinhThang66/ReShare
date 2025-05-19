package com.example.reshare.domain.repository

import com.example.reshare.domain.model.User
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(
        userId: String
    ): Resource<User>
}