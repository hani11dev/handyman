package com.example.handyapp.register.domain.use_case

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if(password.length < 3){
            return ValidationResult(
                successful = false,
                errorMessage = "password can't be < 8"
            )
        }
        return ValidationResult(
            successful = true,
        )
    }
}