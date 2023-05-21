package com.example.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val barcode : String = "",
    val imgUrl: String = "",
    val name : String = "",
    val price: Double = 0.0,
    val discountPercentage : Double = 0.0,
    val category : String = "",
    val bannerUrl : String = "",
    val description : String = "",
)
