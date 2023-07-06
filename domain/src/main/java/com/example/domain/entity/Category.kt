package com.example.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val _id: Int = 0,
    val categoryName: String = "",
    val categoryImgUrl: String = "",
    var color: String = "",
    val thumbnail: String = "",
) : Parcelable
