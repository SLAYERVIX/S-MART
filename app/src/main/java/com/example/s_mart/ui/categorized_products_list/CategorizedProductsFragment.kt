package com.example.s_mart.ui.categorized_products_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.s_mart.core.adapters.CategoryListAdapter
import com.example.s_mart.databinding.FragmentCategorizedProductsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategorizedProductsFragment : Fragment() {

    private var _binding: FragmentCategorizedProductsBinding? = null
    private val binding get() = _binding!!

    private val args: CategorizedProductsFragmentArgs by navArgs()

    private val viewModel: CategorizedProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategorizedProductsBinding.inflate(inflater, container, false)

        val adapter = CategoryListAdapter()
        binding.rvProducts.adapter = adapter

        getCategoryItems(adapter)

        binding.tvCategoryName.text = args.category.categoryName

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getCategoryItems(adapter: CategoryListAdapter) {
        lifecycleScope.launch {
            viewModel.categorizedProducts(args.category.categoryName).collect {
                it?.let {
                    Log.d("rabbit", "getCategoryItems: $it")
                    adapter.submitList(it)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}