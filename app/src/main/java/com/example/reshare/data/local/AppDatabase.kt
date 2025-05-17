package com.example.reshare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reshare.data.local.post.PostDao
import com.example.reshare.data.local.post.PostEntity
import com.example.reshare.data.local.post.PostTypeConverters

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
@TypeConverters(PostTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val postDao: PostDao
}