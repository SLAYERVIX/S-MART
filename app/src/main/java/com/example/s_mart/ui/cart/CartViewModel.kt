package com.example.s_mart.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Product
import com.example.domain.entity.ProductResponse
import com.example.domain.repo.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val localRepository: LocalRepository) :
    ViewModel() {

    fun getAllCartProducts(): Flow<List<Product>> = localRepository.getCartProducts()

    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        localRepository.insertProduct(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        localRepository.deleteProduct(product)
    }

    fun clearCart() = viewModelScope.launch(Dispatchers.IO) {
        localRepository.clearCart()
    }
}