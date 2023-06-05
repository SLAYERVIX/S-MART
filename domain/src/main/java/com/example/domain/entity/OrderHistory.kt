package com.example.domain.entity

data class OrderHistory(
    val products : MutableList<Product> = mutableListOf(),
    var totalPrice : Double = 0.0,
    var date : String = ""
)
