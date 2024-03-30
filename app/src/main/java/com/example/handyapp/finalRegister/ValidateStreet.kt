package com.example.handyapp.finalRegister

import com.example.handyapp.register.domain.use_case.ValidationResult

class ValidateStreet {
    fun excute(street: String): ValidationResult {
        if(street.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "street is empty"
            )

        }
        return ValidationResult(
            successful = true
        )
    }
}
