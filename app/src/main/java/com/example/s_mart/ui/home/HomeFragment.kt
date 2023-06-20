package com.example.s_mart.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.entity.Category
import com.example.domain.entity.Client
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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategories()
        setupTodayDeal()
        setupPoints()

        binding.tvDiscount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profile)
        }
        viewModel.firebaseAuth.currentUser?.let {
            binding.tvName.text = it.displayName
        }
    }

    private fun setupPoints() {
        viewModel.clientDocument.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val client = snapshot.toObject(Client::class.java)
                client?.let {
                    binding.tvPoints.text = client.points.toString()
                }
            }
        }
    }

    private fun setupTodayDeal() {
        viewModel.dealOfTheDayDocument.get().addOnCompleteListener { task_1 ->
            task_1.addOnSuccessListener { snapshot_1 ->
                val productID = snapshot_1.toObject(TodayDeal::class.java)?.productID
                if (productID != null) {
                    viewModel.productCollection.document(productID).get()
                        .addOnSuccessListener { snapshot ->
                            val product = snapshot.toObject(Product::class.java)
                            if (product != null) {
                                _binding?.let { // Wrap the code inside a null-check block
                                    Glide.with(it.imageView3).load(product.imgUrl)
                                        .into(it.imageView3)

                                    it.tvDealName.text = product.name

                                    it.tvDiscount.visibility = View.VISIBLE

                                    val priceText = String.format(
                                        getString(R.string.egp),
                                        calcDiscount(
                                            product.price,
                                            product.discountPercentage
                                        ).toString()
                                    )

                                    it.tvDealPrice.text = priceText
                                    it.tvDiscount.text =
                                        getString(R.string.egp, product.price.toString())

//                                    it.tvDiscount.text = getString(
//                                        R.string.price_with_discount,
//                                        getString(R.string.egp),
//                                        "%.2f".format(calcDiscount(product.price, product.discountPercentage))
//                                    )

                                }
                            }
                        }
                }
            }
        }
    }

    private fun setupCategories() {
        val categoryAdapter = CategoryAdapter(this)
        binding.rvCategories.adapter = categoryAdapter

        viewModel.categoryCollection.addSnapshotListener { value, error ->

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