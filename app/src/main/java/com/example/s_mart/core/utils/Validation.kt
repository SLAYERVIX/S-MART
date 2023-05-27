package com.example.s_mart.core.utils

import android.util.Patterns

object Validation {
    // 0 -> is Empty
    // 1 -> is too short
     fun validatePassword(password: String, callback: (Int) -> Unit) : Boolean {
        if (password.isEmpty()) {
            callback(0)
            return false
        }
        if (password.length < 6) {
            callback(1)
            return false
        }
        return true
    }

    // 0 -> is Empty
    // 1 -> didn't match email pattern
    fun validateEmail(email: String, callback: (Int) -> Unit) : Boolean {
        if (email.isEmpty()) {
            callback(0)
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback(1)
            return false
        }
        return true
    }
}