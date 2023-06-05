package com.example.s_mart.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Voucher
import com.example.s_mart.core.callbacks.RedeemCallBack
import com.example.s_mart.core.differs.VoucherDiffItemCallback
import com.example.s_mart.databinding.ItemRvRewardBinding

class RewardsAdapter (private val voucherCallback : RedeemCallBack) :
    ListAdapter<Voucher, RewardsAdapter.ViewHolder>(VoucherDiffItemCallback()) {
    inner class ViewHolder(private val binding: ItemRvRewardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Voucher) {
            binding.voucher = item
            binding.executePendingBindings()
            binding.btnRedeem.setOnClickListener {
                voucherCallback.onRedeemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvRewardBinding.inflate(
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