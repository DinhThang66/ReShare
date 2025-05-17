package com.example.reshare.data.remote

import com.example.reshare.data.remote.dto.AddCommentRequest
import com.example.reshare.data.remote.dto.CommentDto
import com.example.reshare.data.remote.dto.CommentsDto
import com.example.reshare.data.remote.dto.LoginRequest
import com.example.reshare.data.remote.dto.LoginResponse
import com.example.reshare.data.remote.dto.PostListDto
import com.example.reshare.data.remote.dto.RegisterRequest
import com.example.reshare.data.remote.dto.RegisterResponse
import com.example.reshare.data.remote.dto.StreamTokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("api/chat/token")
    suspend fun getStreamToken(): Response<StreamTokenDto>

    // Get Post
    @GET("api/post")
    suspend fun getAllPosts(): PostListDto

    // Comment Endpoint
    @GET("api/comment/{postId}")
    suspend fun getCommentsByPost(
        @Path("postId") postId: String
    ): Response<CommentsDto>

    @POST("api/comment")
    suspend fun addComment(
        @Body request: AddCommentRequest
    ): Response<CommentDto>

    companion object {
        const val BASE_URL = "http://192.168.0.101:5000/"
    }
}