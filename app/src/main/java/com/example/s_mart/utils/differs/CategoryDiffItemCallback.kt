package com.example.s_mart.utils.differs

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.Category

class CategoryDiffItemCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}