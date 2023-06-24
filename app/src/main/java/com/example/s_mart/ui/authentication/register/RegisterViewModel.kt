package com.example.s_mart.ui.authentication.register

import androidx.lifecycle.ViewModel
import com.example.domain.repo.FirebaseAuthRepository
import com.example.domain.states.RegistrationResult
import com.example.domain.states.UpdateProfileResult
import com.example.s_mart.R
import com.example.s_mart.core.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    // Validates the email format and displays corresponding error messages
    fun validateEmailFormat(email: String, messageCallback: (Int) -> Unit): Boolean {
        val result = Validation.validateEmail(email)

        if (result == 0) {
            messageCallback(R.string.field_cannot_be_empty)
            return false
        }
        else if (result == 1) {
            messageCallback(R.string.you_must_enter_a_valid_email)
            return false
        }

        return true
    }

    // Validates the password format and displays corresponding error messages
    fun validatePasswordFormat(password: String, messageCallback: (Int) -> Unit): Boolean {
        val result = Validation.validatePassword(password)

        if (result == 0) {
            messageCallback(R.string.field_cannot_be_empty)
            return false
        }
        else if (result == 1) {
            messageCallback(R.string.minimum_password_chars)
            return false
        }

        return true
    }

    // Creates a user with email, password, and display name
    fun createUserWithEmailAndPassword(email: String, password: String): Flow<RegistrationResult> {
        return firebaseAuthRepository.createAccountWithEmailAndPassword(email, password)
    }

    // Updates the display name for the user
    fun updateProfileData(displayName: String): Flow<UpdateProfileResult> {
        return firebaseAuthRepository.updateProfileData(displayName)
    }
}