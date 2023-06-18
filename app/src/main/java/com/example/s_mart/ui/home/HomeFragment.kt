package com.example.s_mart.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.entity.Category
import com.example.domain.entity.Product
import com.example.domain.entity.TodayDeal
import com.example.s_mart.R
import com.example.s_mart.core.adapters.CategoryAdapter
import com.example.s_mart.core.callbacks.CategoryCallback
import com.example.s_mart.core.utils.calcDiscount
import com.example.s_mart.databinding.FragmentHomeBinding
import com.example.s_mart.ui.SmartViewModel

class HomeFragment : Fragment(), CategoryCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SmartViewModel by activityViewModels()

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
        viewModel.firebaseAuth.currentUser?.let {
            binding.tvName.text = it.displayName
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupTodayDeal() {
        viewModel.dealOfTheDayDocument.get().addOnSuccessListener {
            val productID = it.toObject(TodayDeal::class.java)?.productID
            if (productID != null) {
                viewModel.productCollection.document(productID).get()
                    .addOnSuccessListener { snapshot ->
                        val product = snapshot.toObject(Product::class.java)

                        Glide.with(binding.imageView3).load(product?.imgUrl)
                            .into(binding.imageView3)

                        binding.tvDealName.text = product?.name

                        binding.tvDiscount.visibility = View.VISIBLE
                        product?.let {
                            binding.tvDealPrice.text =
                                "EGP " + calcDiscount(product.price, product.discountPercentage)
                            binding.tvDiscount.text = "EGP " + product.price
                        }
                    }
            }
        }
    }

    private fun setupCategories() {
        val categoryAdapter = CategoryAdapter(this)
        binding.rvCategories.adapter = categoryAdapter

        viewModel.categoryCollection.addSnapshotListener { value, error ->
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