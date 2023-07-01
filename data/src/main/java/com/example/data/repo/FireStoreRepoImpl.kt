package com.example.data.repo

import com.example.data.remote.FireStoreService
import com.example.domain.entity.Category
import com.example.domain.entity.Client
import com.example.domain.entity.Product
import com.example.domain.repo.FireStoreRepository
import kotlinx.coroutines.flow.Flow

class FireStoreRepoImpl (private val fireStoreService: FireStoreService) : FireStoreRepository {
    override fun retrieveCategories(): Flow<List<Category>> {
       return fireStoreService.retrieveCategories()
    }

    override fun retrieveDealOfTheDay(): Flow<Product?> {
        return fireStoreService.retrieveDealOfTheDay()
    }

    override fun retrieveClient(): Flow<Client> {
        return fireStoreService.retrieveClient()
    }
}