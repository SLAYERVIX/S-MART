package com.example.s_mart.core.callbacks

import com.example.domain.entity.Product

interface CartCallback {
    fun onDeleteClicked(item: Product)
}