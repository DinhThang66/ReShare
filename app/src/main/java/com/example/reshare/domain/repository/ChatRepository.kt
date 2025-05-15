package com.example.reshare.domain.repository

interface ChatRepository {
    suspend fun getStreamToken(): Result<String>
}