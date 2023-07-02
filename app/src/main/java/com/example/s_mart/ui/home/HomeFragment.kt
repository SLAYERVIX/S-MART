package com.example.s_mart.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.entity.Category
import com.example.s_mart.R
import com.example.s_mart.core.adapters.CategoryAdapter
import com.example.s_mart.core.callbacks.CategoryCallback
import com.example.s_mart.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), CategoryCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categoryAdapter = CategoryAdapter(this)

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.apply {
            rvCategories.adapter = categoryAdapter
            tvDiscount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategories()
        setupTodayDeal()
        setupPoints()
        setupUsername()

        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profile)
        }
    }

    private fun setupUsername() {
        homeViewModel.retrieveFirebaseUser()?.let {
            binding.tvName.text = it.displayName
        }
    }

    private fun setupPoints() {
        lifecycleScope.launch {
            homeViewModel.retrieveClient.collect { client ->
               _binding?.client = client
            }
        }
    }

    private fun setupTodayDeal() {
        lifecycleScope.launch {
            homeViewModel.retrieveDealOfTheDay().collect { deal ->
                deal?.let { product ->
                    binding.product = product
                    Glide.with(requireContext()).load(product.imgUrl).into(binding.ivDeal)
                }
            }
        }
    }

    private fun setupCategories() {
        lifecycleScope.launch {
            homeViewModel.retrieveCategories().collect {
                if (it.isNotEmpty()) {
                    categoryAdapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCategoryClicked(item: Category) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToCategoryListFragment(item)
        )
    }
}