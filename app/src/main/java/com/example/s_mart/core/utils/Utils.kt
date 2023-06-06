package com.example.s_mart.core.utils

fun calcDiscount(price: Double, discountPercentage: Double): Double {
    return price - (price * discountPercentage)
}