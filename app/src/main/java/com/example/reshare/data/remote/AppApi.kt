package com.example.reshare.data.remote

import com.example.reshare.data.remote.dto.LoginRequest
import com.example.reshare.data.remote.dto.LoginResponse
import com.example.reshare.data.remote.dto.PostsDto
import com.example.reshare.data.remote.dto.RegisterRequest
import com.example.reshare.data.remote.dto.RegisterResponse
import com.example.reshare.data.remote.dto.StreamTokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("api/chat/token")
    suspend fun getStreamToken(): Response<StreamTokenDto>

    // Get Post
    @GET("api/post")
    suspend fun getAllPosts(): Response<PostsDto>

    companion object {
        const val BASE_URL = "http://192.168.0.100:5000/"
    }
}