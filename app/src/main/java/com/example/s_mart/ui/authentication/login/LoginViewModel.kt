package com.example.s_mart.ui.authentication.login

import androidx.lifecycle.ViewModel
import com.example.domain.repo.FirebaseAuthRepository
import com.example.domain.states.LoginResult
import com.example.s_mart.core.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject
constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    fun isEmptyField(field : String) : Boolean = Validation.isEmptyField(field)
    fun validateEmailFormat(email : String) : Boolean = Validation.doesMatchEmailPattern(email)
    fun validatePasswordLength(password : String) : Boolean = Validation.validatePasswordLength(password)

    // Signs in the user with email and password
    fun signInWithEmailAndPassword(email: String, password: String, ) : Flow<LoginResult> {
        return firebaseAuthRepository.loginWithEmailAndPassword(email, password)
    }
}
