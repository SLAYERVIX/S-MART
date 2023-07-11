package com.example.s_mart.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Product
import com.example.s_mart.core.differs.DealsDiffUtilCallback
import com.example.s_mart.databinding.ItemRvFeaturedDealBinding

class DealsAdapter : ListAdapter<Product, DealsAdapter.ViewHolder>(DealsDiffUtilCallback()) {

    inner class ViewHolder(private val binding:  ItemRvFeaturedDealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {

            binding.product = item
            binding.executePendingBindings()

            Glide.with(binding.root.context)
                .load(item.imgUrl)
                .into(binding.ivDeal)

            binding.tvDiscount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvFeaturedDealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}