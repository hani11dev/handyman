package com.example.handyapp.register.domain.components

sealed class RegistrationEvent {
    data class EmailChanged(val email: String): RegistrationEvent()
    data class PasswordChanged(val password: String): RegistrationEvent()
    data class ConfirmPasswordChanged(val repeatedPassword: String): RegistrationEvent()
    data class PhoneNumberChanged(val phoneNumber: String): RegistrationEvent()
    data class TermsChanged(val accepted: Boolean): RegistrationEvent()

    object Submit: RegistrationEvent()
}