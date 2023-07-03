package com.example.s_mart.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.ProfileItem
import com.example.s_mart.core.differs.ProfileDiffItemCallback
import com.example.s_mart.databinding.ItemProfileBinding

class ProfileAdapter :
    ListAdapter<ProfileItem, ProfileAdapter.ViewHolder>(ProfileDiffItemCallback()) {
    inner class ViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProfileItem) {
            binding.tvTitle.text = item.title
            val resourceId = item.resourceID

            // Retrieve the drawable from resources
            val drawable = ContextCompat.getDrawable(binding.root.context, resourceId)

            // Set the drawable to the TextView's compound drawables
            binding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProfileBinding.inflate(
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