package com.example.s_mart.ui.profile

import androidx.lifecycle.ViewModel
import com.example.domain.entity.ProfileItem
import com.example.domain.repo.FirebaseAuthRepository
import com.example.s_mart.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    val profileItems = listOf(
        ProfileItem(
            UUID.randomUUID().toString(),
            "Orders",
            R.drawable.order
        ),
        ProfileItem(
            UUID.randomUUID().toString(),
            "Vouchers",
            R.drawable.baseline_local_offer_24
        )
    )

    val currentUser = firebaseAuthRepository.retrieveCurrentUser()

    fun firebaseSignOut() : Boolean {
        return firebaseAuthRepository.firebaseSignOut()
    }
}