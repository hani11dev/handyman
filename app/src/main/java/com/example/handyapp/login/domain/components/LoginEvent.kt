package com.example.handyapp.register.domain.components

sealed class LoginEvent {
    data class EmailChanged(val email: String): LoginEvent()
    data class PasswordChanged(val password: String): LoginEvent()
    object Submit: LoginEvent()
}