package com.example.s_mart.ui.checkout

sealed class PaymentState {
    object Idle : PaymentState()
    object Successful : PaymentState()
}
