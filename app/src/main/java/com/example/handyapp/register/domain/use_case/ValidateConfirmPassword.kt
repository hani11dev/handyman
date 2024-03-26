package com.example.handyapp.register.domain.use_case

class ValidateConfirmPassword {
    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if(password != repeatedPassword){
            return ValidationResult(
                successful = false,
                errorMessage = "passwords doesn't match"
            )
        }
        return ValidationResult(
            successful = true,
        )
    }
}