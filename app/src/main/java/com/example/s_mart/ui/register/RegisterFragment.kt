package com.example.s_mart.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.example.s_mart.core.utils.Validation
import com.example.s_mart.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnRegister.setOnClickListener {
            tryRegistering()
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun tryRegistering() {
        resetHelpersText()

        val displayName = binding.etDisplayName.text
            .toString()
            .trim()
            .lowercase()
            .split(" ")
            .joinToString(" ") { join ->
                join.replaceFirstChar { it.uppercaseChar() }
            }

        val email = binding.etEmail.text.toString().trim().lowercase()
        val password = binding.etPassword.text.toString().trim()

        if (!validateEmail(email)) return
        if (!validatePassword(password)) return
        if (!validateDisplayName(displayName)) return

        setLoginButtonEnabled(false)

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build()

                        it.updateProfile(profileUpdates)
                            .addOnCompleteListener { updateTask ->
                                setLoginButtonEnabled(true)
                                if (updateTask.isSuccessful) {
                                    findNavController().navigate(R.id.action_registerFragment_to_main)
                                } else {
                                    // Handle profile update failure
                                }
                            }
                    }
                }
                task.addOnFailureListener {
                    setLoginButtonEnabled(true)
                }
            }
    }

    private fun validateDisplayName(displayName: String): Boolean {
        if (displayName.isEmpty()) {
            setDisplayNameHelperText(getString(R.string.field_cannot_be_empty))
            return false
        }
        return true
    }

    private fun resetHelpersText() {
        binding.containerEmail.helperText = null
        binding.containerPassword.helperText = null
        binding.containerDisplayName.helperText = null
    }

    private fun setEmailHelperText(message: String) {
        binding.containerEmail.helperText = message
    }

    private fun setPasswordHelperText(message: String) {
        binding.containerPassword.helperText = message
    }

    private fun setDisplayNameHelperText(message: String) {
        binding.containerDisplayName.helperText = message
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
}