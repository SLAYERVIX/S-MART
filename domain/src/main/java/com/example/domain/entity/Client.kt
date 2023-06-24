package com.example.domain.entity

import com.example.domain.entity.Cart
import com.example.domain.entity.OrderHistory
import com.example.domain.entity.Voucher

data class Client(
    val cart : Cart = Cart(),
    val orderHistory : OrderHistory = OrderHistory(),
    var points : Int = 0,
    var vouchers: MutableList<Voucher> = mutableListOf()
)