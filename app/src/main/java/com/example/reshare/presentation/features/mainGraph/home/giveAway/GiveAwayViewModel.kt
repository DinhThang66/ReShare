package com.example.reshare.presentation.features.mainGraph.home.giveAway

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GiveAwayViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(GiveAwayState())
    val state = _state

    fun onEvent(event: GiveAwayUiEvent) {
        when(event) {
            is GiveAwayUiEvent.AddImages -> {
                val images = event.images.take(2)
                _state.update { it.copy(selectedImages = images) }
            }
            is GiveAwayUiEvent.RemoveImage -> {
                val updatedList =
                    _state.value.selectedImages.toMutableList()
                updatedList.remove(event.uri)
                _state.update { it.copy(selectedImages = updatedList) }
            }

            is GiveAwayUiEvent.SetTitle -> {
                _state.update { it.copy(title = event.title) }
            }
            is GiveAwayUiEvent.SetDescription -> {
                _state.update { it.copy(description = event.description) }
            }
            is GiveAwayUiEvent.SetPickupTime -> {
                _state.update { it.copy(pickupTime = event.pickupTime) }
            }
            is GiveAwayUiEvent.SetInstructions -> {
                _state.update { it.copy(instructions = event.instructions) }
            }
            is GiveAwayUiEvent.SetPostType -> {
                _state.update { it.copy(postType = event.type) }
            }
            is GiveAwayUiEvent.SetProductType -> {
                _state.update { it.copy(productType = event.type) }
            }
        }
    }
}