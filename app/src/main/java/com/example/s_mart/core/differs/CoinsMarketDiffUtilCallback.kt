package com.example.s_mart.core.differs

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.entity.CoinsProduct

class CoinsMarketDiffUtilCallback : DiffUtil.ItemCallback<CoinsProduct>() {
    override fun areItemsTheSame(oldItem: CoinsProduct, newItem: CoinsProduct): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: CoinsProduct, newItem: CoinsProduct): Boolean {
        return oldItem == newItem
    }
}