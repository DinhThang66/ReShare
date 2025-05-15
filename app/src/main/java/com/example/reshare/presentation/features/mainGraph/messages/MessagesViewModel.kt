package com.example.reshare.presentation.features.mainGraph.messages

import androidx.lifecycle.ViewModel
import com.example.reshare.domain.usecase.auth.GetStreamTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val getStreamTokenUseCase: GetStreamTokenUseCase
) : ViewModel() {
//    var streamToken by mutableStateOf<String?>(null)
//        private set
//    var error by mutableStateOf<String?>(null)
//
//    init {
//        fetchStreamToken()
//    }

}