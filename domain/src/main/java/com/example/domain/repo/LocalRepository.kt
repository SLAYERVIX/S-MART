package com.example.domain.repo

import com.example.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun getCartProducts(): Flow<List<Product>>

    suspend fun clearCart()

    suspend fun insertProduct(product: Product)

    suspend fun deleteProduct(product: Product)
}