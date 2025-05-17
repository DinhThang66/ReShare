package com.example.reshare.data.mapper

import com.example.reshare.data.local.post.PostEntity
import com.example.reshare.data.remote.dto.CommentDto
import com.example.reshare.data.remote.dto.PostDto
import com.example.reshare.data.remote.dto.UserDto
import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.model.Post
import com.example.reshare.domain.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun UserDto.toDomain(): User {
    return User(
        id = _id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        profilePic = profilePic
    )
}

fun PostDto.toDomain(): Post {
    return Post(
        id = _id,
        content = content,
        createdBy = createdBy.copy(email = "").toDomain(),
        images = images,
        likes = likes,
        commentsCount = commentsCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
fun PostEntity.toDomain(): Post {
    val gson = Gson()
    return Post(
        id = id,
        content = content,
        createdBy = gson.fromJson(createdBy, User::class.java),
        images = gson.fromJson(images, object : TypeToken<List<String>>() {}.type),
        likes = gson.fromJson(likes, object : TypeToken<List<String>>() {}.type),
        commentsCount = commentsCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
fun PostDto.toEntity(): PostEntity {
    val gson = Gson()
    return PostEntity(
        id = _id,
        content = content,
        createdBy = gson.toJson(createdBy),
        images = gson.toJson(images),
        likes = gson.toJson(likes),
        commentsCount = commentsCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CommentDto.toDomain(): Comment {
    return Comment(
        id = _id,
        postId = postId,
        content = content,
        createdBy = createdBy.copy(email = "").toDomain(),
        likes = likes,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}