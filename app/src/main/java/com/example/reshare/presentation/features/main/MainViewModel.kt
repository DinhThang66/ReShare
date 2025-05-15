package com.example.reshare.presentation.features.main

import androidx.lifecycle.ViewModel
import com.example.reshare.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    val userName: Flow<String?> = userPreferences.userFName
    val token: Flow<String?> = userPreferences.userToken
}