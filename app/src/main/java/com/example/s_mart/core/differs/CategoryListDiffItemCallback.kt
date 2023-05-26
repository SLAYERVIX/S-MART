package com.example.s_mart.core.differs

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.entity.Product

class CategoryListDiffItemCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}