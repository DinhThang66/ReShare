package com.example.reshare.data.mapper

import com.example.reshare.data.local.post.PostEntity
import com.example.reshare.data.local.product.ProductEntity
import com.example.reshare.data.remote.dto.CategorizedProductDto
import com.example.reshare.data.remote.dto.CommentDto
import com.example.reshare.data.remote.dto.PostDto
import com.example.reshare.data.remote.dto.ProductDto
import com.example.reshare.data.remote.dto.UserDto
import com.example.reshare.domain.model.CategorizedProducts
import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.model.Post
import com.example.reshare.domain.model.Product
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
        images = images,
        createdBy = createdBy.copy(email = "").toDomain(),
        commentsCount = commentsCount,
        likesCount = likesCount,
        likedByCurrentUser = likedByCurrentUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
fun PostEntity.toDomain(): Post {
    val gson = Gson()
    return Post(
        id = id,
        content = content,
        images = gson.fromJson(images, object : TypeToken<List<String>>() {}.type),
        createdBy = gson.fromJson(createdBy, UserDto::class.java).copy(email = "").toDomain(),
        commentsCount = commentsCount,
        likesCount = likesCount,
        likedByCurrentUser = likedByCurrentUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
fun PostDto.toEntity(): PostEntity {
    val gson = Gson()
    return PostEntity(
        id = _id,
        content = content,
        images = gson.toJson(images),
        createdBy = gson.toJson(createdBy),
        commentsCount = commentsCount,
        likesCount = likesCount,
        likedByCurrentUser = likedByCurrentUser,
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

fun ProductEntity.toDomain(): Product {
    val gson = Gson()
    return Product(
        id = id,
        title = title,
        description = description,
        images = gson.fromJson(images, object : TypeToken<List<String>>() {}.type),

        pickupTimes = pickupTimes,
        pickupInstructions = pickupInstructions,

        locationLat = locationLat,
        locationLng = locationLng,

        tag = tag,
        type = type,
        quantity = quantity,

        originalPrice = originalPrice,
        discountPercent = discountPercent,
        storeInfo = storeInfo,
        createdBy = gson.fromJson(createdBy, UserDto::class.java).copy(email = "").toDomain(),
        updatedAt = updatedAt
    )
}
fun ProductDto.toEntity(): ProductEntity {
    val gson = Gson()
    return ProductEntity(
        id = _id,
        title = title,
        description = description,
        images = gson.toJson(images),

        pickupTimes = pickupTimes,
        pickupInstructions = pickupInstructions,

        locationLat = location.coordinates[1],
        locationLng = location.coordinates[0],

        tag = type,
        type = productType,
        quantity = quantity,

        originalPrice = originalPrice,
        discountPercent =  discountPercent,
        storeInfo = storeInfo,

        createdBy = gson.toJson(createdBy),
        updatedAt = updatedAt
    )
}
fun ProductDto.toDomain(): Product {
    return Product(
        id = _id,
        title = title,
        description = description,
        images = images,

        pickupTimes = pickupTimes,
        pickupInstructions = pickupInstructions,

        locationLat = location.coordinates[1],
        locationLng = location.coordinates[0],

        tag = type,
        type = productType,
        quantity = quantity,

        originalPrice = originalPrice,
        discountPercent =  discountPercent,
        storeInfo = storeInfo,

        createdBy = createdBy.copy(email = "").toDomain(),
        updatedAt = updatedAt
    )
}

fun CategorizedProductDto.toDomain(): CategorizedProducts {
    return CategorizedProducts(
        freeFood = freeFood.map { it.toDomain() },
        nonFood = nonFood.map { it.toDomain() },
        reducedFood = reducedFood.map { it.toDomain() },
        want = want.map { it.toDomain() }
    )
}