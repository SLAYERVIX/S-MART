package com.example.s_mart.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Product
import com.example.s_mart.core.differs.CategoryListDiffItemCallback
import com.example.s_mart.databinding.ItemRvCategoryListBinding

class CategoryListAdapter :
    ListAdapter<Product, CategoryListAdapter.ViewHolder>(CategoryListDiffItemCallback()) {
    inner class ViewHolder(private val binding: ItemRvCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.product = item
            binding.executePendingBindings()

            Glide.with(binding.ivProduct).load(item.imgUrl).into(binding.ivProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}