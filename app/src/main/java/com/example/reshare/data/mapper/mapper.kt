package com.example.reshare.data.mapper

import com.example.reshare.data.remote.dto.CommentDto
import com.example.reshare.data.remote.dto.PostDto
import com.example.reshare.data.remote.dto.UserDto
import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.model.Post
import com.example.reshare.domain.model.User

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