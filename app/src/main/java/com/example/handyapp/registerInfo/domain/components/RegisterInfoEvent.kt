package com.example.handyapp.register.domain.components

sealed class RegisterInfoEvent {
    data class FirstNameChanged(val firstName: String): RegisterInfoEvent()
    data class LastNameChanged(val lastName: String): RegisterInfoEvent()
    data class YearChanged(val year: String): RegisterInfoEvent()
    data class MonthChanged(val month: String): RegisterInfoEvent()
    data class DayChanged(val day: String): RegisterInfoEvent()
    data class WilayaChanged(val wilaya: String): RegisterInfoEvent()
    data class CityChanged(val city: String): RegisterInfoEvent()
    data class StreetChanged(val street: String): RegisterInfoEvent()
    object Submit: RegisterInfoEvent()
}

