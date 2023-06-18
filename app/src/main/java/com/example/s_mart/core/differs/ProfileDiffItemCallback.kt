package com.example.s_mart.core.differs

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.entity.ProfileItem

class ProfileDiffItemCallback : DiffUtil.ItemCallback<ProfileItem>() {
    override fun areItemsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean {
        return oldItem == newItem
    }
}