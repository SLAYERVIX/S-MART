package com.example.s_mart.core.utils

import android.util.Patterns

object Validation {
    fun isEmptyField(data: String): Boolean {
        return data.isEmpty()
    }

    fun doesMatchEmailPattern(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePasswordLength(password: String): Boolean {
        return password.length < 6
    }

    fun validateCreditCardNumbersLength(card: String): Boolean {
        return card.length == 16
    }

    fun validateCreditCardFormat(card: String): Boolean {
        return Regex("^[0-9]+$").matches(card)
    }

    fun validateCvvLength(cvv : String): Boolean {
        return cvv.length == 3
    }

    fun validateCvvFormat(cvv: String): Boolean {
        return Regex("^[0-9]+$").matches(cvv)
    }
}