package com.example.s_mart.core.differs

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.entity.Category

class CategoryDiffItemCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}