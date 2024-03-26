package com.example.handyapp.register.domain.use_case

class ValidateTerms {
    fun execute(acceptsTerms: Boolean): ValidationResult {
        if(!acceptsTerms){
            return ValidationResult(
                successful = false,
                errorMessage = "you have to accept terms"
            )
        }
        return ValidationResult(
            successful = true,
        )
    }
}