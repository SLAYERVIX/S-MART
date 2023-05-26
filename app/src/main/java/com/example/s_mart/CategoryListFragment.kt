package com.example.s_mart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.domain.entity.Product
import com.example.s_mart.core.Constants
import com.example.s_mart.core.adapters.CategoryListAdapter
import com.example.s_mart.databinding.FragmentCategoryListBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class CategoryListFragment : Fragment() {

    private var _binding: FragmentCategoryListBinding? = null

    private val args: CategoryListFragmentArgs by navArgs()

    private lateinit var fireStoreDatabase: FirebaseFirestore
    private lateinit var reference: CollectionReference
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireStoreDatabase = FirebaseFirestore.getInstance()
        reference = fireStoreDatabase.collection(Constants.PRODUCTS_REF)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)

        val adapter = CategoryListAdapter()
        binding.rvProducts.adapter = adapter

        getCategoryItems(adapter)

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getCategoryItems(adapter: CategoryListAdapter) {
        val query = reference.whereEqualTo("category", args.category.categoryName)
        query.get().addOnSuccessListener {
            adapter.submitList(it.toObjects(Product::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}