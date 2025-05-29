package com.example.reshare.data.local.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity (
    @PrimaryKey
    val id: String,

    val title: String,
    val description: String,
    val images: String,                 // JSON string

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

    val createdBy: String,
    val updatedAt: String
)