package com.example.reshare.domain.usecase.googleMaps

import com.example.reshare.domain.model.PlaceSuggestion
import com.example.reshare.domain.repository.PlacesRepository
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchPlacesUseCase @Inject constructor(
    private val repository: PlacesRepository
) {
    suspend operator fun invoke(query: String): Resource<List<PlaceSuggestion>>  {
        return  repository.searchPlaces(query)
    }
}