package com.example.reshare.data.local.post

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
//
//interface PostDao {
//    @Query("SELECT * FROM posts")
//    fun getAllPosts(): Flow<List<PostEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertPosts(posts: List<PostEntity>
//}