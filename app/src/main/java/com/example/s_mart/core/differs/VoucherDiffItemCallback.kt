package com.example.s_mart.core.differs

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.entity.Voucher

class VoucherDiffItemCallback : DiffUtil.ItemCallback<Voucher>() {
    override fun areItemsTheSame(oldItem: Voucher, newItem: Voucher): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Voucher, newItem: Voucher): Boolean {
        return oldItem == newItem
    }
}