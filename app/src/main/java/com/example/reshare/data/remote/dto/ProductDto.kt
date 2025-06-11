package com.example.reshare.data.remote.dto

data class ProductDto(
    val _id: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val pickupTimes: String,
    val pickupInstructions: String? = null,
    val location: Location,
    val distance: Double,
    val type: String,  // "free", "paid", "reduced", "want"
    val productType: String,    // "food", "non-food"
    val quantity: Int,
    val originalPrice: Double? = null,
    val discountPercent: Int? = null,
    val storeInfo: String? = null,
    val createdBy: UserDto,
    val createdAt: String,
    val updatedAt: String
)

data class Location(
    val coordinates: List<Double>,
    val type: String
)


data class CategorizedProductDto(
    val freeFood: List<ProductDto>,
    val nonFood: List<ProductDto>,
    val reducedFood: List<ProductDto>,
    val want: List<ProductDto>
)

data class ProductListDto(
    val products: List<ProductDto>
)
