package com.example.s_mart.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater,container,false)
        // creating arraylist of fragments required for the adapter
        val fragmentList = arrayListOf(
            PageOneFragment(),
            PageTwoFragment(),
            PageThreeFragment()
        )

        // assigning on boarding adapter object
        val adapter = OnBoardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        with(binding) {
            // Initializing view pager adapter
            viewpager.adapter = adapter

            // Attach dots indicator to view pager
            // dot1.attachTo(viewpager)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}