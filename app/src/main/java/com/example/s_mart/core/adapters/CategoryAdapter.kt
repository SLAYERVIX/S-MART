package com.example.s_mart.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Category
import com.example.s_mart.core.callbacks.CategoryCallback
import com.example.s_mart.core.differs.CategoryDiffItemCallback
import com.example.s_mart.databinding.ItemRvCategoryBinding

class CategoryAdapter(private val categoryCallback: CategoryCallback) :
    ListAdapter<Category, CategoryAdapter.ViewHolder>(CategoryDiffItemCallback()) {
    inner class ViewHolder(private val binding: ItemRvCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.category = item

            Glide.with(binding.root.context)
                .load(item.categoryImgUrl)
                .into(binding.ivCategory)

            binding.root.setOnClickListener {
                categoryCallback.onCategoryClicked(item)
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvCategoryBinding.inflate(
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