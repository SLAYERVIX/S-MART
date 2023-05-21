package com.example.s_mart.core.callbacks

import com.example.domain.entity.Category

interface CategoryCallback {
    fun onCategoryClicked(item : Category)
}