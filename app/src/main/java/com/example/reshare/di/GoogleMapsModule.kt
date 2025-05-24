package com.example.reshare.di

import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.GoogleMapsApi
import com.example.reshare.data.repository.ChatRepositoryImpl
import com.example.reshare.data.repository.PlacesRepositoryImpl
import com.example.reshare.domain.repository.ChatRepository
import com.example.reshare.domain.repository.PlacesRepository
import com.example.reshare.presentation.utils.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleMapsModule {
    private const val GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/"
    private const val KEY = ApiConstants.GMP_KEY
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GOOGLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleMapsApi(retrofit: Retrofit): GoogleMapsApi {
        return retrofit.create(GoogleMapsApi::class.java)
    }

    @Provides
    @Named("maps_api_key")
    fun provideMapsApiKey(): String {
        return KEY
    }

    @Provides
    fun providePlacesRepository(api: GoogleMapsApi): PlacesRepository {
        return PlacesRepositoryImpl(api, KEY)
    }
}