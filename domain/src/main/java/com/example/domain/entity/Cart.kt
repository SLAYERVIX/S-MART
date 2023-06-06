package com.example.domain.entity

data class Cart(
    val products : MutableList<Product> = mutableListOf(),
    var totalPrice : Double = 0.0,
    var appliedVoucher: Voucher = Voucher()
)
