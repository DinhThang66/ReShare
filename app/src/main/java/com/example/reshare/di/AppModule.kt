package com.example.reshare.di

import android.content.Context
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.interceptor.AuthInterceptor
import com.example.reshare.data.repository.AuthRepositoryImpl
import com.example.reshare.data.repository.ChatRepositoryImpl
import com.example.reshare.data.repository.PostRepositoryImpl
import com.example.reshare.domain.repository.AuthRepository
import com.example.reshare.domain.repository.ChatRepository
import com.example.reshare.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    fun provideAuthApi(): AppApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AppApi.BASE_URL)
            .client(client)
            .build()
            .create(AppApi::class.java)
    }
     */

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreferences: UserPreferences): AuthInterceptor {
        return AuthInterceptor(userPreferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor) // interceptor chèn Cookie
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(client: OkHttpClient): AppApi {
        return Retrofit.Builder()
            .baseUrl(AppApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AppApi::class.java)
    }

    // Repository bindings
    @Provides
    fun provideAuthRepository(api: AppApi): AuthRepository {
        return AuthRepositoryImpl(api)
    }
    @Provides
    fun provideChatRepository(api: AppApi): ChatRepository {
        return ChatRepositoryImpl(api)
    }
    @Provides
    fun providePostRepository(api: AppApi): PostRepository {
        return PostRepositoryImpl(api)
    }
}