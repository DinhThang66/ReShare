package com.example.reshare.presentation.features.mainGraph.home.itemDetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(

): ViewModel(){
    private val _state = MutableStateFlow(ItemDetailState())
    val state: StateFlow<ItemDetailState> = _state

    fun onEvent(event: ItemDetailUiEvent) {
        when (event) {
            is ItemDetailUiEvent.SetProduct -> {
                _state.update { it.copy(product = event.product) }
            }
        }
    }
}