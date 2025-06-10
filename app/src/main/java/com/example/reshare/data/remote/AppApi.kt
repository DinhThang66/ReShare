package com.example.reshare.data.remote

import com.example.reshare.data.remote.dto.AddCommentRequest
import com.example.reshare.data.remote.dto.CategorizedProductDto
import com.example.reshare.data.remote.dto.CommentDto
import com.example.reshare.data.remote.dto.CommentsDto
import com.example.reshare.data.remote.dto.LikeResponseDto
import com.example.reshare.data.remote.dto.LocationRequest
import com.example.reshare.data.remote.dto.LoginDto
import com.example.reshare.data.remote.dto.LoginRequest
import com.example.reshare.data.remote.dto.PostDto
import com.example.reshare.data.remote.dto.PostListDto
import com.example.reshare.data.remote.dto.ProductListDto
import com.example.reshare.data.remote.dto.RegisterDto
import com.example.reshare.data.remote.dto.RegisterRequest
import com.example.reshare.data.remote.dto.StreamTokenDto
import com.example.reshare.data.remote.dto.UpdateLocationDto
import com.example.reshare.data.remote.dto.UserDto
import com.example.reshare.presentation.utils.ApiConstants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AppApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginDto>
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterDto>
    @PATCH("api/auth/update-location")
    suspend fun updateLocation(
        @Body body: LocationRequest
    ): Response<UpdateLocationDto>
    @GET("api/chat/token")
    suspend fun getStreamToken(): Response<StreamTokenDto>

    // Post Endpoint
    @GET("api/post")
    suspend fun getPosts(@Query("page") page: Int): PostListDto
    @PUT("api/post/{id}/like")
    suspend fun toggleLike(@Path("id") postId: String): LikeResponseDto
    @Multipart
    @POST("api/post")       // multipart/form-data
    suspend fun createPost(
        @Part("content") content: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<PostDto>

    // Comment Endpoint
    @GET("api/comment/{postId}")
    suspend fun getCommentsByPost(@Path("postId") postId: String): Response<CommentsDto>
    @POST("api/comment")
    suspend fun addComment(@Body request: AddCommentRequest): Response<CommentDto>

    // User Endpoint
    @GET("api/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String) : Response<UserDto>

    // Product Endpoint
    @GET("api/product/categorized")
    suspend fun getCategorizedProducts(): CategorizedProductDto
    @GET("api/product")
    suspend fun getAllNearbyProducts(): ProductListDto

    companion object {
        const val BASE_URL = ApiConstants.BASE_URL
    }
}