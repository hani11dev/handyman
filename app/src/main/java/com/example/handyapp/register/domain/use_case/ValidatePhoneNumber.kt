package com.example.handyapp.register.domain.use_case

class ValidatePhoneNumber {
    fun execute(phoneNumber: String): ValidationResult {
        if(phoneNumber.length != 10){
            return ValidationResult(
                successful = false,
                errorMessage = "phone number must have 10 digits"
            )
        }
        if(!(phoneNumber.startsWith("06")) && !(phoneNumber.startsWith("05")) && !(phoneNumber.startsWith("07"))){
            return ValidationResult(
                successful = false,
                errorMessage = "Invalid"
            )
        }
        return ValidationResult(
            successful = true,
        )
    }
}