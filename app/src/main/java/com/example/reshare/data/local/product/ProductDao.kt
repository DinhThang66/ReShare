package com.example.reshare.data.local.product

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ProductDao {
    @Query("""
        SELECT * FROM products 
        WHERE tag = 'free' AND type = 'food' 
        ORDER BY updatedAt DESC 
        LIMIT 20
    """)
    suspend fun getFreeFood(): List<ProductEntity>

    @Query("""
        SELECT * FROM products 
        WHERE type = 'non-food' 
        ORDER BY updatedAt DESC 
        LIMIT 20
    """)
    suspend fun getNonFood(): List<ProductEntity>

    @Query("""
        SELECT * FROM products 
        WHERE tag = 'reduced' AND type = 'food' 
        ORDER BY updatedAt DESC 
        LIMIT 20
    """)
    suspend fun getReducedFood(): List<ProductEntity>

    @Query("""
        SELECT * FROM products 
        WHERE tag = 'wanted' 
        ORDER BY updatedAt DESC 
        LIMIT 20
    """)
    suspend fun getWant(): List<ProductEntity>

    @Upsert
    suspend fun upsertProducts(products: List<ProductEntity>)
}