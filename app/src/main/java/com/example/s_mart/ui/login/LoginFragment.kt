package com.example.s_mart.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.example.s_mart.core.utils.Validation
import com.example.s_mart.databinding.FragmentLoginBinding
import com.example.s_mart.ui.SmartViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SmartViewModel by activityViewModels()
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
                        viewModel.reInitializeDocuments()
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

    private fun validateEmail(email: String): Boolean {
        return Validation.validateEmail(email) { result ->
            if (result == 0) {
                setEmailHelperText(getString(R.string.field_cannot_be_empty))
            }
            if (result == 1) {
                setEmailHelperText(getString(R.string.you_must_enter_a_valid_email))
            }
        }
    }
    private fun validatePassword(password: String): Boolean {
        return Validation.validatePassword(password) { result ->
            if (result == 0) {
                setPasswordHelperText(getString(R.string.field_cannot_be_empty))
            }
            else {
                setPasswordHelperText(getString(R.string.minimum_password_chars))
            }
        }
    }

    private fun setLoginButtonEnabled(isEnabled: Boolean) {
        binding.btnLogin.isEnabled = isEnabled
    }

    private fun resetHelpersText() {
        binding.containerEmail.helperText = null
        binding.containerPassword.helperText = null
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