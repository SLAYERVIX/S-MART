package com.example.s_mart.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentPageThreeBinding

class PageThreeFragment : Fragment() {

    private var _binding: FragmentPageThreeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageThreeBinding.inflate(inflater,container,false)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_login)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}