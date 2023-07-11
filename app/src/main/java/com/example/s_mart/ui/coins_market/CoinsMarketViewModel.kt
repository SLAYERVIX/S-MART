package com.example.s_mart.ui.coins_market

import androidx.lifecycle.ViewModel
import com.example.domain.entity.Client
import com.example.domain.entity.CoinsProduct
import com.example.domain.repo.FireStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CoinsMarketViewModel @Inject constructor(
    private val fireStoreRepository: FireStoreRepository,
) : ViewModel() {
    fun retrieveCoinsProducts() : Flow<List<CoinsProduct>> {
        return fireStoreRepository.retrieveCoinsProducts()
    }

    fun retrieveClient() : Flow<Client?> {
        return fireStoreRepository.retrieveClient()
    }
}