package com.example.data.repo

import com.example.data.remote.FirebaseAuthService
import com.example.domain.repo.FirebaseAuthRepository
import com.example.domain.states.RegistrationResult
import com.example.domain.states.UpdateProfileResult
import kotlinx.coroutines.flow.Flow


class FirebaseAuthRepoImpl(private val firebaseAuthService: FirebaseAuthService) : FirebaseAuthRepository {
    override fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<RegistrationResult> {
        return firebaseAuthService.createAccountWithEmailAndPassword(email,password)
    }

    override fun updateProfileData(displayName: String): Flow<UpdateProfileResult> {
        return firebaseAuthService.updateProfileData(displayName)
    }
}