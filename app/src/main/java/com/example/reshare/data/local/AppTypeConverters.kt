package com.example.reshare.data.local

import androidx.room.TypeConverter
import com.example.reshare.domain.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromUser(user: User): String = gson.toJson(user)

    @TypeConverter
    fun toUser(data: String): User = gson.fromJson(data, User::class.java)

    @TypeConverter
    fun fromList(list: List<String>): String = gson.toJson(list)

    @TypeConverter
    fun toList(data: String): List<String> {
        val type  = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, type)
    }
}