package com.example.data.repo

import com.example.data.local.CartDao
import com.example.domain.entity.Product
import com.example.domain.repo.LocalRepository
import kotlinx.coroutines.flow.Flow

class LocalRepoImpl(private val cartDao: CartDao) : LocalRepository {
    override fun getCartProducts(): Flow<List<Product>> {
        return cartDao.getCartProducts()
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override suspend fun insertProduct(product: Product) {
        cartDao.insertProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        cartDao.deleteProduct(product)
    }
}