package com.example.domain.entity

data class Client(
    val cart : Cart = Cart(),
    val points : Int = 0
)