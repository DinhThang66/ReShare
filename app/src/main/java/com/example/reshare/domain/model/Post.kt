package com.example.reshare.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: String,
    val content: String,
    val createdBy: User,
    val images: List<String>,
    val likes: List<String>,
    val commentsCount: Int,
    val createdAt: String,
    val updatedAt: String,
): Parcelable