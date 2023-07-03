package com.example.s_mart.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Voucher
import com.example.s_mart.core.differs.VoucherDiffItemCallback
import com.example.s_mart.databinding.ItemRvVouchersHorizontalBinding

class VoucherAdapter : ListAdapter<Voucher, VoucherAdapter.ViewHolder>(VoucherDiffItemCallback()) {
    var onApplyClicked: ((Voucher) -> Unit)? = null
    inner class ViewHolder(private val binding: ItemRvVouchersHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Voucher) {
            binding.voucher = item
            binding.executePendingBindings()
            binding.button2.setOnClickListener {
                onApplyClicked?.invoke(item)
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