package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.Product
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {
    @Query("SELECT * FROM products_table")
    fun getCartProducts(): Flow<List<Product>>

    @Query("DELETE FROM products_table")
    suspend fun clearCart()

    @Insert
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
}