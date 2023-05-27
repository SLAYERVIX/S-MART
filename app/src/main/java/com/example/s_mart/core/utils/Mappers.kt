package com.example.s_mart.core.utils

import com.example.domain.entity.Product
import com.example.domain.entity.ProductResponse

object Mappers {
    fun mapProductResponseToProduct(productResponse: ProductResponse) : Product {
        return Product(
            _id = 0,
            barcode = productResponse.barcode,
            imgUrl = productResponse.imgUrl,
            name = productResponse.name,
            price = productResponse.price,
            discountPercentage = productResponse.discountPercentage,
            category = productResponse.category,
            bannerUrl = productResponse.bannerUrl,
            description = productResponse.description
        )
    }
}