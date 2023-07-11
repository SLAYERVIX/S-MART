package com.example.domain.repo

import com.example.domain.entity.Category
import com.example.domain.entity.Client
import com.example.domain.entity.CoinsProduct
import com.example.domain.entity.Product
import com.example.domain.entity.Voucher
import kotlinx.coroutines.flow.Flow

interface FireStoreRepository {
    fun retrieveCategories(): Flow<List<Category>>
    fun retrieveDeals(): Flow<List<Product>?>
    fun retrieveClient(): Flow<Client?>
    fun retrieveCategorizedProducts(categoryName : String): Flow<List<Product>?>
    fun retrieveVouchers(): Flow<List<Voucher>>
    fun createClientInstance()
    fun updateClient(client: Client)
    fun deleteProductFromCart(product: Product)
    fun clearProductsFromCart()
    fun retrieveProductByBarcode(barcode: String): Flow<Product>
    fun addProductToCart(product: Product)
    fun updateAppliedVoucher(voucher: Voucher)
    fun retrieveCoinsProducts() : Flow<List<CoinsProduct>>
}