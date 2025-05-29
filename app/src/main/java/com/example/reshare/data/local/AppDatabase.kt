package com.example.reshare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reshare.data.local.post.PostDao
import com.example.reshare.data.local.post.PostEntity
import com.example.reshare.data.local.product.ProductDao
import com.example.reshare.data.local.product.ProductEntity

@Database(
    entities = [PostEntity::class, ProductEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val postDao: PostDao
    abstract val productDao: ProductDao
}