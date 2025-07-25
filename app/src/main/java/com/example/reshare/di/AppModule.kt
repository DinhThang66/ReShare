package com.example.reshare.di

import android.content.Context
import androidx.room.Room
import com.example.reshare.data.local.AppDatabase
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.data.local.post.PostDao
import com.example.reshare.data.local.product.ProductDao
import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.interceptor.AuthInterceptor
import com.example.reshare.data.repository.AuthRepositoryImpl
import com.example.reshare.data.repository.ChatRepositoryImpl
import com.example.reshare.data.repository.CommentRepositoryImpl
import com.example.reshare.data.repository.PostRepositoryImpl
import com.example.reshare.data.repository.ProductRepositoryImpl
import com.example.reshare.data.repository.RequestsRepositoryImpl
import com.example.reshare.data.repository.UserRepositoryImpl
import com.example.reshare.domain.repository.AuthRepository
import com.example.reshare.domain.repository.ChatRepository
import com.example.reshare.domain.repository.CommentRepository
import com.example.reshare.domain.repository.PostRepository
import com.example.reshare.domain.repository.ProductRepository
import com.example.reshare.domain.repository.RequestsRepository
import com.example.reshare.domain.repository.UserRepository
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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "reShare.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePostDao(appDatabase: AppDatabase): PostDao {
        return appDatabase.postDao
    }
    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao
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
    fun providePostRepository(api: AppApi, dao: PostDao): PostRepository {
        return PostRepositoryImpl(api, dao)
    }
    @Provides
    fun provideCommentRepository(api: AppApi): CommentRepository {
        return CommentRepositoryImpl(api)
    }
    @Provides
    fun provideUserRepository(api: AppApi): UserRepository {
        return UserRepositoryImpl(api)
    }
    @Provides
    fun provideProductRepository(api: AppApi, dao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(api, dao)
    }
    @Provides
    fun provideRequestsRepository(api: AppApi): RequestsRepository {
        return RequestsRepositoryImpl(api)
    }
}