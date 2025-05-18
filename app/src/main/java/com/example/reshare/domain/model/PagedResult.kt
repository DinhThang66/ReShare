package com.example.reshare.domain.model

data class PagedResult<T>(
    val data: List<T>,
    val currentPage: Int = 1,
    val totalPages: Int = 1
)