package com.example.s_mart.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.entity.Category
import com.example.domain.entity.Product
import com.example.domain.entity.TodayDeal
import com.example.s_mart.R
import com.example.s_mart.core.adapters.CategoryAdapter
import com.example.s_mart.core.callbacks.CategoryCallback
import com.example.s_mart.core.utils.Constants
import com.example.s_mart.core.utils.calcDiscount
import com.example.s_mart.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(), CategoryCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fireStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupCategories()
        setupTodayDeal()

        binding.tvDiscount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profile)
        }
        firebaseAuth.currentUser?.let {
            binding.tvName.text = it.displayName
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupTodayDeal() {
        val dayDealReference = fireStore.collection(Constants.TODAY_DEAL_REF).document(Constants.DEAL)
        dayDealReference.get().addOnSuccessListener { it ->
            val productID = it.toObject(TodayDeal::class.java)

            if (productID != null) {
                val ref = fireStore.collection(Constants.PRODUCTS_REF).document(productID.productID)
                ref.get().addOnSuccessListener { product ->

                    val prod = product.toObject(Product::class.java)

                    Glide.with(binding.imageView3).load(prod?.imgUrl).into(binding.imageView3)

                    binding.tvDealName.text = prod?.name

                    binding.tvDiscount.visibility = View.VISIBLE
                    prod?.let {
                        binding.tvDealPrice.text =
                            "EGP " + calcDiscount(prod.price, prod.discountPercentage)
                        binding.tvDiscount.text = "EGP " + prod.price
                    }
                }
            }
        }
    }

    private fun setupCategories() {
        val categoryAdapter = CategoryAdapter(this)
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

    override fun onCategoryClicked(item: Category) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToCategoryListFragment(item)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
}