package com.example.s_mart.home

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.domain.Category
import com.example.domain.Product
import com.example.domain.TodayDeal
import com.example.s_mart.databinding.FragmentHomeBinding
import com.example.s_mart.utils.Constants
import com.example.s_mart.utils.adapters.CategoryAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireStore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupCategories()

        val dayDealReference = fireStore.collection(Constants.TODAY_DEAL_REF).document("deal")
        dayDealReference.get().addOnSuccessListener {
            val productID = it.toObject(TodayDeal::class.java)

            val ref =
                fireStore.collection(Constants.PRODUCTS_REF).document(productID!!.productID)
            ref.get()
                .addOnSuccessListener { product ->

                    val prod = product.toObject(Product::class.java)
                    binding.tvDealName.text = prod?.name
                    binding.tvDealPrice.text = "EGP " + prod?.price.toString()
                    binding.tvDiscount.text = "EGP " + prod?.price.toString()
                }
        }

        binding.tvDiscount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupCategories() {
        val categoryAdapter = CategoryAdapter()
        binding.rvCategories.adapter = categoryAdapter

        val categoryReference = fireStore.collection(Constants.CATEGORIES_REF)

        categoryReference.addSnapshotListener { value, error ->
            if (error != null) {
                // Handle the error appropriately
                Log.d("rabbit", "onCreateView: $error")
                return@addSnapshotListener
            }

            val categories = mutableListOf<Category>()
            if (value != null) {
                for (snapshot in value) {
                    if (snapshot != null && snapshot.exists()) {
                        val category = snapshot.toObject(Category::class.java)
                        categories.add(category)
                    }
                    else {
                        // Handle the case when the snapshot is null or does not exist
                    }
                }
            }

            categoryAdapter.submitList(categories)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}