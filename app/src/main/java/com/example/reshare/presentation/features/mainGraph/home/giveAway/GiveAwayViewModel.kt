package com.example.reshare.presentation.features.mainGraph.home.giveAway

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.product.CreateProductUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class GiveAwayViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase,
    @ApplicationContext private val appContext: Context
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
            is GiveAwayUiEvent.Submit -> {
                submitProduct()
            }
        }
    }

    private fun submitProduct() {
        viewModelScope.launch {
            val currentState = _state.value
            val missingFields = mutableListOf<String>()

            if (currentState.title.isBlank()) missingFields.add("Title")
            if (currentState.description.isBlank()) missingFields.add("Description")
            if (currentState.pickupTime.isBlank()) missingFields.add("Pickup time")
            if (currentState.instructions.isBlank()) missingFields.add("Pickup instructions")
            if (currentState.selectedImages.isEmpty()) missingFields.add("At least one image")

            if (missingFields.isNotEmpty()) {
                val message = "Please provide: ${missingFields.joinToString(", ")}"
                _state.update { it.copy(error = message) }
                return@launch
            }

            _state.update { it.copy(isSubmitting = true, error = null, isSuccess = false) }

            // Tạo PartMap
            val partMap = mapOf(
                "title"                 to state.value.title.toPlainRequest(),
                "description"           to state.value.description.toPlainRequest(),
                "pickupTimes"           to state.value.pickupTime.toPlainRequest(),
                "pickupInstructions"    to state.value.instructions.toPlainRequest(),
                "type"                  to state.value.postType.toPlainRequest(),
                "productType"           to state.value.productType.toPlainRequest(),
            )

            // Tạo list MultipartBody.Part cho ảnh
            val imageParts = currentState.selectedImages
                .asReversed()
                .mapIndexed { index, uri ->
                val inputStream = appContext.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: byteArrayOf()
                inputStream?.close()

                val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData(
                    name = "images",
                    filename = "image_${System.currentTimeMillis()}_$index.jpg",
                    body = requestBody
                )
            }

            // Gọi use case
            when (val result = createProductUseCase(partMap, imageParts)) {
                is Resource.Success -> {
                    _state.update { it.copy(isSubmitting = false, isSuccess = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isSubmitting = false, error = result.message) }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}


val plain = "text/plain".toMediaTypeOrNull()

fun String.toPlainRequest(): RequestBody = this.toRequestBody(plain)