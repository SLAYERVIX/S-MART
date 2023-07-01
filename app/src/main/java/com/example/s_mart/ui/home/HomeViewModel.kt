package com.example.s_mart.ui.home

import androidx.lifecycle.ViewModel
import com.example.domain.entity.Category
import com.example.domain.entity.Client
import com.example.domain.entity.Product
import com.example.domain.repo.FireStoreRepository
import com.example.domain.repo.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject
constructor(
    private val fireStoreRepository: FireStoreRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    fun retrieveCategories(): Flow<List<Category>> = fireStoreRepository.retrieveCategories()

    fun retrieveDealOfTheDay(): Flow<Product?> = fireStoreRepository.retrieveDealOfTheDay()

    fun retrieveFirebaseUser() : FirebaseUser? = firebaseAuthRepository.retrieveCurrentUser()

    fun retrieveClient() : Flow<Client> = fireStoreRepository.retrieveClient()
}