package com.example.s_mart.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Voucher
import com.example.s_mart.core.callbacks.VoucherCallback
import com.example.s_mart.core.differs.VoucherDiffItemCallback
import com.example.s_mart.databinding.ItemRvVouchersHorizontalBinding

class VoucherAdapter (private val voucherCallback: VoucherCallback) :
    ListAdapter<Voucher, VoucherAdapter.ViewHolder>(VoucherDiffItemCallback()) {
    inner class ViewHolder(private val binding: ItemRvVouchersHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Voucher) {
            binding.voucher = item
            binding.executePendingBindings()
            binding.button2.setOnClickListener {
                voucherCallback.onApplyClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvVouchersHorizontalBinding.inflate(
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