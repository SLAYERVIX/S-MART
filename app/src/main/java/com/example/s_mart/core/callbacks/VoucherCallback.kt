package com.example.s_mart.core.callbacks

import com.example.domain.entity.Voucher

interface VoucherCallback {
    fun onApplyClicked(item : Voucher)
}