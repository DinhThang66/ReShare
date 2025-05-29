package com.example.reshare.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val pickupTimes: String,
    val pickupInstructions: String?,
    val locationLat: Double,
    val locationLng: Double,
    val tag: String,
    val type: String,
    val quantity: Int,
    val originalPrice: Double? = null,
    val discountPercent: Int? = null,
    val storeInfo: String? = null,
    val createdBy: User,
    val updatedAt: String
): Parcelable

data class CategorizedProducts(
    val freeFood: List<Product>,
    val nonFood: List<Product>,
    val reducedFood: List<Product>,
    val want: List<Product>
)