package com.example.s_mart.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categoryAdapter = CategoryAdapter()

    private val homeViewModel: HomeViewModel by viewModels()

    private val dealsAdapter = DealsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.apply {
            rvCategories.adapter = categoryAdapter
            rvDeals.adapter = dealsAdapter

            ivProfile.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profile)
            }
        }

        onCategoryClicked()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategories()
        setupDeals()
        setupUsername()
    }

    private fun setupUsername() {
        homeViewModel.retrieveFirebaseUser()?.let {
            binding.tvName.text = it.displayName
        }
    }

    private fun setupDeals() {
        lifecycleScope.launch {
            homeViewModel.retrieveDeals().collect { deals ->
                deals?.let {
                    dealsAdapter.submitList(it)
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

    private fun onCategoryClicked() {
        categoryAdapter.onCategoryClicked = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCategoryListFragment(it)
            )
        }
    }
}