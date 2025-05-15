package com.example.reshare.domain.usecase.auth

import com.example.reshare.domain.repository.ChatRepository
import javax.inject.Inject

class GetStreamTokenUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(): Result<String> {
        return repository.getStreamToken()
    }
}