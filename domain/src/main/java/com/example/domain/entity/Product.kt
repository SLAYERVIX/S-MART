package com.example.domain.entity

data class Product(
    var _id: String = "",
    val barcode : String = "",
    val imgUrl: String = "",
    val name : String = "",
    val price: Double = 0.0,
    val discountPercentage : Double = 0.0,
    val category : String = "",
    val bannerUrl : String = "",
    val description : String = "",
)