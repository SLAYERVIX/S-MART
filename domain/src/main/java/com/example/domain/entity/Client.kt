package com.example.domain.entity

data class Client(
    val cart : Cart = Cart(),
    val orderHistory : OrderHistory = OrderHistory(),
    var points : Int = 0,
    var voucher: Voucher = Voucher()
)