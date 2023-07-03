package com.example.s_mart.ui.cart

import androidx.lifecycle.ViewModel
import com.example.domain.entity.Client
import com.example.domain.entity.Product
import com.example.domain.entity.Voucher
import com.example.domain.repo.BluetoothRepository
import com.example.domain.repo.FireStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel
@Inject constructor(
    private val fireStoreRepository: FireStoreRepository,
    private val bluetoothRepository: BluetoothRepository
) : ViewModel() {
    val barcode : SharedFlow<String> get() = bluetoothRepository.retrieveBarcodeFlow()

    init {
        startScan()
    }

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

    fun calculatePriceAfterDiscount(client: Client) : Double {
        client.cart.totalPrice -= (client.cart.totalPrice * client.cart.appliedVoucher.discount)
        return client.cart.totalPrice
    }

    fun startScan() = bluetoothRepository.startScan()
    fun stopScan() = bluetoothRepository.stopScan()
}