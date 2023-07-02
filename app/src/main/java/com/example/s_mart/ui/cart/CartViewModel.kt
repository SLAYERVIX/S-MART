package com.example.s_mart.ui.cart

import androidx.lifecycle.ViewModel
import com.example.domain.entity.Client
import com.example.domain.entity.Product
import com.example.domain.entity.Voucher
import com.example.domain.repo.FireStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CartViewModel
@Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {
    fun removeProductFromCart(product: Product) = fireStoreRepository.deleteProductFromCart(product)

    fun clearCart() = fireStoreRepository.clearProductsFromCart()

    fun retrieveProductByBarcode(barcode: String): Flow<Product> {
        return fireStoreRepository.retrieveProductByBarcode(barcode)
    }

    fun addProductToCart(product: Product) {
        fireStoreRepository.addProductToCart(product)
    }

    fun updateAppliedVoucher(voucher: Voucher) {
        fireStoreRepository.updateAppliedVoucher(voucher)
    }

    fun retrieveClient() : Flow<Client?> {
        return fireStoreRepository.retrieveClient()
    }
}