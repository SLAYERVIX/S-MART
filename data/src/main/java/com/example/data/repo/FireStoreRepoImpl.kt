package com.example.data.repo

import com.example.data.remote.FireStoreService
import com.example.domain.entity.Category
import com.example.domain.entity.Client
import com.example.domain.entity.CoinsProduct
import com.example.domain.entity.Product
import com.example.domain.entity.Voucher
import com.example.domain.repo.FireStoreRepository
import kotlinx.coroutines.flow.Flow

class FireStoreRepoImpl (private val fireStoreService: FireStoreService) : FireStoreRepository {
    override fun retrieveCategories(): Flow<List<Category>> {
       return fireStoreService.retrieveCategories()
    }

    override fun retrieveDeals(): Flow<List<Product>?> {
        return fireStoreService.retrieveDeals()
    }

    override fun retrieveClient(): Flow<Client?> {
        return fireStoreService.retrieveClient()
    }

    override fun retrieveCategorizedProducts(categoryName : String): Flow<List<Product>?> {
        return fireStoreService.retrieveCategorizedProducts(categoryName)
    }

    override fun retrieveVouchers(): Flow<List<Voucher>> {
        return fireStoreService.retrieveVouchers()
    }

    override fun createClientInstance() {
        fireStoreService.createClientInstance()
    }

    override fun updateClient(client: Client) {
        fireStoreService.updateClient(client)
    }

    override fun deleteProductFromCart(product: Product) {
        fireStoreService.deleteProductFromCart(product)
    }

    override fun clearProductsFromCart() {
        fireStoreService.clearProductsFromCart()
    }

    override fun retrieveProductByBarcode(barcode: String): Flow<Product> {
        return fireStoreService.retrieveProductByBarcode(barcode)
    }

    override fun addProductToCart(product: Product) {
        fireStoreService.addProductToCart(product)
    }

    override fun updateAppliedVoucher(voucher: Voucher) {
        fireStoreService.updateAppliedVoucher(voucher)
    }

    override fun retrieveCoinsProducts(): Flow<List<CoinsProduct>> {
        return fireStoreService.retrieveCoinsProducts()
    }
}