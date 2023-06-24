package com.example.domain.states

sealed class UpdateProfileResult {
    object Success : UpdateProfileResult()
    object Completed : UpdateProfileResult()
}
