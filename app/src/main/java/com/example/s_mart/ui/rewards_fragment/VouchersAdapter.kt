package com.example.s_mart.ui.rewards_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Voucher
import com.example.s_mart.core.differs.VoucherDiffItemCallback
import com.example.s_mart.databinding.ItemRvVouchersBinding

class VouchersAdapter  :
    ListAdapter<Voucher, VouchersAdapter.ViewHolder>(VoucherDiffItemCallback()) {

    var onRedeemClicked: ((Voucher) -> Unit)? = null
    inner class ViewHolder(private val binding: ItemRvVouchersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Voucher) {
            binding.voucher = item
            binding.executePendingBindings()
            binding.btnRedeem.setOnClickListener {
                onRedeemClicked?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvVouchersBinding.inflate(
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