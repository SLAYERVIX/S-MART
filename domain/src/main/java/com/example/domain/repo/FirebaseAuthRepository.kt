package com.example.domain.repo

import com.example.domain.states.LoginResult
import com.example.domain.states.RegistrationResult
import com.example.domain.states.UpdateProfileResult
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    fun createAccountWithEmailAndPassword(email: String, password: String): Flow<RegistrationResult>
    fun updateProfileData(displayName: String): Flow<UpdateProfileResult>
    fun loginWithEmailAndPassword(email: String, password: String): Flow<LoginResult>
}