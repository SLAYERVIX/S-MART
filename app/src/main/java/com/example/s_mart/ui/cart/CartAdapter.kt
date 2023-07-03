package com.example.s_mart.ui.cart

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Product
import com.example.s_mart.core.differs.CartDiffItemCallback
import com.example.s_mart.databinding.ItemRvCartBinding


class CartAdapter() :
    ListAdapter<Product, CartAdapter.ViewHolder>(CartDiffItemCallback()) {
    var onItemDeleteClicked: ((Product) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemRvCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.product = item
            binding.executePendingBindings()

            binding.tvRealPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            if (item.discountPercentage == 0.0) {
                binding.tvRealPrice.visibility = View.GONE
            }

            binding.btnDelete.setOnClickListener {
                onItemDeleteClicked?.invoke(item)
            }

            Glide.with(binding.ivProduct).load(item.imgUrl).into(binding.ivProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}