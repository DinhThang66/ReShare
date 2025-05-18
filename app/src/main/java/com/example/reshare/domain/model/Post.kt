package com.example.reshare.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: String,
    val content: String,
    val images: List<String>,
    val createdBy: User,
    val commentsCount: Int,
    val likesCount: Int,
    val likedByCurrentUser: Boolean,
    val createdAt: String,
    val updatedAt: String,
): Parcelable