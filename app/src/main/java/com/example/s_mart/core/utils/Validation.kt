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
}