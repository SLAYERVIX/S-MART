package com.example.s_mart.core.callbacks

import com.example.domain.entity.Voucher

interface RedeemCallBack {
    fun onRedeemClicked(item : Voucher)
}