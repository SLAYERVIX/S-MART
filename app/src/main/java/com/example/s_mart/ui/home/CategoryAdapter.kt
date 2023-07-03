package com.example.s_mart.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Category
import com.example.s_mart.core.differs.CategoryDiffItemCallback
import com.example.s_mart.databinding.ItemRvCategoryBinding

class CategoryAdapter :
    ListAdapter<Category, CategoryAdapter.ViewHolder>(CategoryDiffItemCallback()) {
    var onCategoryClicked: ((Category) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemRvCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {

            binding.category = item
            binding.executePendingBindings()
            Glide.with(binding.root.context)
                .load(item.categoryImgUrl)
                .into(binding.ivCategory)

            binding.root.setOnClickListener {
                onCategoryClicked?.invoke(item)
            }
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