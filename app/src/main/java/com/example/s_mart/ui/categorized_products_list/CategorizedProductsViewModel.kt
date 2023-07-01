package com.example.s_mart.ui.categorized_products_list

import androidx.lifecycle.ViewModel
import com.example.domain.entity.Product
import com.example.domain.repo.FireStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CategorizedProductsViewModel
@Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {
    fun categorizedProducts(categoryName: String): Flow<List<Product>?> {
        return fireStoreRepository.retrieveCategorizedProducts(categoryName)
    }
}