package com.example.s_mart.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.states.LoginResult
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun attemptLogin() {
        setLoginButtonEnabled(false)
        resetHelpersText()

        val email = binding.etEmail.text.toString().trim().lowercase()
        val password = binding.etPassword.text.toString().trim()

        if (!validateEmailFormat(email)) {
            setLoginButtonEnabled(true)
            return
        }
        if (!validatePasswordFormat(password)) {
            setLoginButtonEnabled(true)
            return
        }

        signInWithEmailAndPassword(email, password)
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        loginViewModel.signInWithEmailAndPassword(email, password)
        lifecycleScope.launch {
            loginViewModel.signInWithEmailAndPassword(email, password).collect {
                when (it) {
                    LoginResult.Completed -> setLoginButtonEnabled(true)
                    LoginResult.ConnectionProblem -> showToast(getString(R.string.connection_problems))
                    LoginResult.Success -> findNavController().navigate(R.id.action_loginFragment_to_main)
                    LoginResult.WrongPassword -> setEmailHelperText(getString(R.string.incorrect_email))
                    LoginResult.WrongUser -> setPasswordHelperText(getString(R.string.incorrect_password))
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun validateEmailFormat(email: String): Boolean {
        return loginViewModel.validateEmailFormat(email) { message ->
            setEmailHelperText(getString(message))
        }
    }

    private fun validatePasswordFormat(password: String): Boolean {
        return loginViewModel.validatePasswordFormat(password) { message ->
            setPasswordHelperText(getString(message))
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
}