package com.example.data.repo

import com.example.data.remote.FirebaseAuthService
import com.example.domain.repo.FirebaseAuthRepository
import com.example.domain.states.LoginResult
import com.example.domain.states.RegistrationResult
import com.example.domain.states.UpdateProfileResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow


class FirebaseAuthRepoImpl(
    private val firebaseAuthService: FirebaseAuthService,
) : FirebaseAuthRepository {
    override fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<RegistrationResult> {
        return firebaseAuthService.createAccountWithEmailAndPassword(email, password)
    }

    override fun updateProfileData(displayName: String): Flow<UpdateProfileResult> {
        return firebaseAuthService.updateProfileData(displayName)
    }

    override fun loginWithEmailAndPassword(email: String, password: String): Flow<LoginResult> {
        return firebaseAuthService.loginWithEmailAndPassword(email, password)
    }

    override fun retrieveCurrentUser(): FirebaseUser? {
        return firebaseAuthService.retrieveCurrentUser()
    }
}