package com.example.domain.states

sealed class RegistrationResult {
    object Success : RegistrationResult()
    object Completed : RegistrationResult()
    object UserCollision : RegistrationResult()
    object ConnectionProblem : RegistrationResult()
}
