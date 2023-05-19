package com.example.domain

data class Product(
    val _id: Int = 0,
    val barcode : String = "",
    val imageUrl: String = "",
    val name : String = "",
    val price: Double = 0.0,
)