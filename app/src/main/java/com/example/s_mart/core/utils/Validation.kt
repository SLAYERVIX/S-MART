package com.example.s_mart.core.utils

import android.util.Patterns

object Validation {

    // 0 -> is Empty
    // 1 -> is too short
    fun validatePassword(password: String): Int {
        return if (password.isEmpty()) {
            0
        }
        else if (password.length < 6) {
            1
        }
        else {
            2
        }
    }

    // 0 -> is Empty
    // 1 -> didn't match email pattern
    fun validateEmail(email: String): Int {
        return if (email.isEmpty()) { 0 }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { 1 }
        else { 2 }
    }
}