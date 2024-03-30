package com.example.handyapp.finalRegister

sealed class FinalRegistrationEvent {
    data class AboutChanged(val about: String): FinalRegistrationEvent()
    data class AverageSalaryChanged(val averageSalary: String): FinalRegistrationEvent()
    data class WorkingAreaChanged(val workingAreas: String): FinalRegistrationEvent()
    data class WilayaChanged(val wilaya: String): FinalRegistrationEvent()
    data class StreetChanged(val street: String): FinalRegistrationEvent()
    data class CityChanged(val city: String): FinalRegistrationEvent()
    object Submit: FinalRegistrationEvent()
}