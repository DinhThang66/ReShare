package com.example.reshare.data.local.post

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    Trong Room, không thể lưu trực tiếp các kiểu dữ liệu:
        - List<String>
        - User (là một data class phức tạp)
 */

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: String,

    val content: String,
    val createdBy: String,          // Json of User
    val images: String,             // Json of List<String>
    val commentsCount: Int,
    val likesCount: Int,
    val likedByCurrentUser: Boolean,
    val createdAt: String,
    val updatedAt: String,
)