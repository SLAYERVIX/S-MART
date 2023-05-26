package com.example.s_mart.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launchWhenCreated {
            delay(3000)

            if (firebaseAuth.currentUser != null) {
                 findNavController().navigate(R.id.action_splashFragment_to_main)
            }
            else {
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }

        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}