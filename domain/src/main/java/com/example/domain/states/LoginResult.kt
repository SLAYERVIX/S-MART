package com.example.domain.states

sealed class LoginResult {
    object Success : LoginResult()
    object Completed : LoginResult()
    object WrongUser : LoginResult()
    object WrongPassword : LoginResult()
    object ConnectionProblem : LoginResult()
}
