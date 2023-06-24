package com.example.s_mart.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private val onBoardingViewModel : OnBoardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        if (onBoardingViewModel.checkUserLoggedIn()) {
            findNavController().navigate(R.id.action_onBoardingFragment_to_main)
        }

        setupAdapter()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupAdapter() {
        val fragmentList = onBoardingViewModel.createFragmentList()

        // assigning on boarding adapter object
        val adapter = OnBoardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        with(binding) {
            viewpager.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}