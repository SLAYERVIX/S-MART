package com.example.s_mart.ui.category_products_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.data.Constants
import com.example.domain.entity.Product
import com.example.s_mart.core.adapters.CategoryListAdapter
import com.example.s_mart.databinding.FragmentCategoryListBinding
import com.example.s_mart.ui.SmartViewModel

class CategoryListFragment : Fragment() {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    private val args: CategoryListFragmentArgs by navArgs()

    private val viewModel: SmartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)

        val adapter = CategoryListAdapter()
        binding.rvProducts.adapter = adapter

        getCategoryItems(adapter)

        binding.tvCategoryName.text = args.category.categoryName

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getCategoryItems(adapter: CategoryListAdapter) {
        val query = viewModel.productCollection.whereEqualTo(Constants.CATEGORY_REF, args.category.categoryName)
        query.get().addOnSuccessListener {
            adapter.submitList(it.toObjects(Product::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}