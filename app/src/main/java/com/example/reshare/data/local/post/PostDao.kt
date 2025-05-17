package com.example.reshare.data.local.post

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

// suspend fun là một hàm hỗ trợ coroutine để thực hiện thao tác bất đồng bộ.

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<PostEntity>

    @Upsert
    suspend fun upsertPosts(posts: List<PostEntity>)
}