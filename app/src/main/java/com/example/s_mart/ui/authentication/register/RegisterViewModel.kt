package com.example.s_mart.ui.authentication.register

import androidx.lifecycle.ViewModel
import com.example.domain.repo.FireStoreRepository
import com.example.domain.repo.FirebaseAuthRepository
import com.example.domain.states.RegistrationResult
import com.example.domain.states.UpdateProfileResult
import com.example.s_mart.core.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {
    fun isEmptyField(field : String) : Boolean = Validation.isEmptyField(field)
    fun validateEmailFormat(email : String) : Boolean = Validation.doesMatchEmailPattern(email)
    fun validatePasswordLength(password : String) : Boolean = Validation.validatePasswordLength(password)

    // Creates a user with email, password, and display name
    fun createUserWithEmailAndPassword(email: String, password: String): Flow<RegistrationResult> {
        return firebaseAuthRepository.createAccountWithEmailAndPassword(email, password)
    }

    // Updates the display name for the user
    fun updateProfileData(displayName: String): Flow<UpdateProfileResult> {
        return firebaseAuthRepository.updateProfileData(displayName)
    }

    fun createClientInstance() = fireStoreRepository.createClientInstance()
}