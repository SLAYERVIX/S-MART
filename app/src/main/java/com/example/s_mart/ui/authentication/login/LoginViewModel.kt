package com.example.s_mart.ui.authentication.login

import androidx.lifecycle.ViewModel
import com.example.domain.states.LoginResult
import com.example.s_mart.R
import com.example.s_mart.core.utils.Validation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    // Represents the login result as a StateFlow
    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

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

    // Signs in the user with email and password
    fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            setLoginResult(LoginResult.Completed)
            task.addOnSuccessListener {
                if (firebaseAuth.currentUser != null) {
                    setLoginResult(LoginResult.Success)
                }
            }
            task.addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthInvalidUserException -> {setLoginResult(LoginResult.WrongUser)}
                    is FirebaseAuthInvalidCredentialsException -> {setLoginResult(LoginResult.WrongPassword)}
                }
            }
        }
    }
    // Sets the registration result value
    private fun setLoginResult(result : LoginResult) {
        _loginResult.value = result
    }
}
