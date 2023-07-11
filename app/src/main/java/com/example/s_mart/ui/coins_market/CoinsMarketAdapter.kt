package com.example.s_mart.ui.coins_market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.CoinsProduct
import com.example.s_mart.core.differs.CoinsMarketDiffUtilCallback
import com.example.s_mart.databinding.ItemRvCoinsMarketBinding

class CoinsMarketAdapter : ListAdapter<CoinsProduct, CoinsMarketAdapter.ViewHolder>(CoinsMarketDiffUtilCallback()) {

    inner class ViewHolder(private val binding: ItemRvCoinsMarketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoinsProduct) {
            binding.coinsProduct = item
            binding.executePendingBindings()

            Glide.with(binding.root.context).load(item.imgUrl).into(binding.ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvCoinsMarketBinding.inflate(
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