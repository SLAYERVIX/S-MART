package com.example.s_mart.ui.rewards_fragment

import androidx.lifecycle.ViewModel
import com.example.domain.entity.Client
import com.example.domain.entity.Voucher
import com.example.domain.repo.FireStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class VoucherViewModel
@Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {
    val vouchers: Flow<List<Voucher>> = fireStoreRepository.retrieveVouchers()

    val client : Flow<Client?> = fireStoreRepository.retrieveClient()

    fun updateClient(voucher: Voucher, client: Client) : Boolean {
        return if (client.points >= voucher.cost) {
            voucher._id = UUID.randomUUID().toString()
            client.points -= voucher.cost
            client.vouchers.add(voucher)
            fireStoreRepository.updateClient(client)
            true
        }
        else {
            false
        }
    }
}