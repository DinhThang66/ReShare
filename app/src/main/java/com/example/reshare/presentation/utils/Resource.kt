package com.example.reshare.presentation.utils

// sealed là một interface sealed(đóng), để kiểm xoát các lớp con hoặc các thực thể kế thừa
// Các lớp con của nó phải được định nghĩa trong cùng một package

// Lớp Resource biểu diễn các trạng thái khác nhau của một tác vụ, trong xử lý bất đồng bộ

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null)
}