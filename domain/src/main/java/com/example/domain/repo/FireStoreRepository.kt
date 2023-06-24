package com.example.domain.repo

import com.example.domain.entity.Category
import com.example.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface FireStoreRepository {
    fun retrieveCategories(): Flow<List<Category>>
    fun retrieveDealOfTheDay(): Flow<Product?>
}