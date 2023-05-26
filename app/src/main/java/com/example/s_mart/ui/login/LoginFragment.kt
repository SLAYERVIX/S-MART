package com.example.s_mart.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun attemptLogin() {
        resetHelpersText()

        val email = binding.etEmail.text.toString().trim().lowercase()
        val password = binding.etPassword.text.toString().trim()

        if (!validateEmail(email)) return
        if (!validatePassword(password)) return

        setLoginButtonEnabled(false)

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    setLoginButtonEnabled(true)
                    if (firebaseAuth.currentUser != null) {
                        findNavController().navigate(R.id.action_loginFragment_to_main)
                    }
                }
                task.addOnFailureListener {
                    setLoginButtonEnabled(true)
                    when (it) {
                        is FirebaseAuthInvalidUserException -> {
                            setEmailHelperText(getString(R.string.incorrect_email))
                            return@addOnFailureListener
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            setPasswordHelperText(getString(R.string.incorrect_password))
                            return@addOnFailureListener
                        }
                    }
                }
            }
    }

    private fun setLoginButtonEnabled(isEnabled : Boolean) {
        binding.btnLogin.isEnabled = isEnabled
    }

    private fun resetHelpersText() {
        binding.containerEmail.helperText = null
        binding.containerPassword.helperText = null
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) {
            setEmailHelperText(getString(R.string.field_cannot_be_empty))
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setEmailHelperText(getString(R.string.you_must_enter_a_valid_email))
            return false
        }
        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isEmpty()) {
            setPasswordHelperText(getString(R.string.field_cannot_be_empty))
            return false
        }
        if (password.length < 6) {
            setPasswordHelperText(getString(R.string.minimum_password_chars))
            return false
        }
        return true
    }

    private fun setEmailHelperText(message: String) {
        binding.containerEmail.helperText = message
    }

    private fun setPasswordHelperText(message: String) {
        binding.containerPassword.helperText = message
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}