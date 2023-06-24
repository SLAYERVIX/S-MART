package com.example.s_mart.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.states.RegistrationResult
import com.example.domain.states.UpdateProfileResult
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflates the layout for the fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // Returns the root view of the fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sets a click listener for the Register button
        binding.btnRegister.setOnClickListener {
            tryRegistering()
        }

        // Sets a click listener for the Login button that navigates to the LoginFragment
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Releases the binding reference when the fragment is destroyed to avoid memory leaks
        _binding = null
    }

    private fun tryRegistering() {
        // Disables the Register button and resets helper texts
        setLoginButtonEnabled(false)
        resetHelpersText()

        // Extracts and processes the display name entered by the user
        val displayName = binding.etDisplayName.text
            .toString()
            .trim()
            .lowercase()
            .split(" ")
            .joinToString(" ") { join ->
                join.replaceFirstChar { it.uppercaseChar() }
            }

        // Extracts the email and password entered by the user
        val email = binding.etEmail.text.toString().trim().lowercase()
        val password = binding.etPassword.text.toString().trim()

        // Validates the display name, email, and password formats
        if (!validateDisplayName(displayName)) {
            setLoginButtonEnabled(true)
            return
        }

        if (!validateEmailFormat(email)) {
            setLoginButtonEnabled(true)
            return
        }

        if (!validatePasswordFormat(password)) {
            setLoginButtonEnabled(true)
            return
        }

        createAccount(email, password, displayName)
    }

    private fun createAccount(email: String, password: String, displayName: String) {
        lifecycleScope.launch {
            registerViewModel.createUserWithEmailAndPassword(email, password).collect { result ->
                when (result) {
                    RegistrationResult.Completed -> {}
                    RegistrationResult.ConnectionProblem -> showToast(getString(R.string.connection_problems))
                    RegistrationResult.Success -> updateProfileData(displayName)
                    RegistrationResult.UserCollision -> setEmailHelperText(getString(R.string.email_already_registered))
                }
            }
        }
    }

    private fun updateProfileData(displayName: String) {
        lifecycleScope.launch {
            registerViewModel.updateProfileData(displayName).collect { result ->
                when (result) {
                    UpdateProfileResult.Completed -> setLoginButtonEnabled(true)
                    UpdateProfileResult.Success -> findNavController().navigate(R.id.action_registerFragment_to_main)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    // Checks if the display name is empty and sets the corresponding helper text
    private fun validateDisplayName(displayName: String): Boolean {
        if (displayName.isEmpty()) {
            setDisplayNameHelperText(getString(R.string.field_cannot_be_empty))
            return false
        }
        return true
    }

    // Resets the helper texts for email, password, and display name fields
    private fun resetHelpersText() {
        binding.apply {
            containerEmail.helperText = null
            containerPassword.helperText = null
            containerDisplayName.helperText = null
        }
    }

    // Sets the helper text for the email field
    private fun setEmailHelperText(message: String) {
        binding.containerEmail.helperText = message
    }

    // Sets the helper text for the password field
    private fun setPasswordHelperText(message: String) {
        binding.containerPassword.helperText = message
    }

    // Sets the helper text for the display name field
    private fun setDisplayNameHelperText(message: String) {
        binding.containerDisplayName.helperText = message
    }

    // Validates the email format using the authenticationViewModel
    // Displays an error message if the format is invalid
    private fun validateEmailFormat(email: String): Boolean {

        return registerViewModel.validateEmailFormat(email) { message ->
            setEmailHelperText(getString(message))
        }
    }

    // Validates the password format using the authenticationViewModel
    // Displays an error message if the format is invalid
    private fun validatePasswordFormat(password: String): Boolean {

        return registerViewModel.validatePasswordFormat(password) { message ->
            setPasswordHelperText(getString(message))
        }
    }

    // Enables or disables the Register button based on the provided flag
    private fun setLoginButtonEnabled(isEnabled: Boolean) {
        binding.btnRegister.isEnabled = isEnabled
    }
}