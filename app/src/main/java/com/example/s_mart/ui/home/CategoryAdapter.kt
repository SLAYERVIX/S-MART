package com.example.s_mart.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
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

    val colors = listOf(
        "#F9BEAD",
        "#FBD96D",
        "#B1EAFD",
        "#CDB9FF",
    )

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
        fun setColor(index : Int) {
            binding.cardView.setCardBackgroundColor(
                ColorStateList.valueOf(Color.parseColor(colors[index]))
            )
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
        holder.setColor(position % colors.size)
    }
}