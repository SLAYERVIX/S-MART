package com.example.s_mart.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class OnBoardingViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun checkUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    // creating arraylist of fragments required for the adapter
    fun createFragmentList(): ArrayList<Fragment> {
        return arrayListOf(
            // PageOneFragment(),
            // PageTwoFragment(),
            PageThreeFragment()
        )
    }
}