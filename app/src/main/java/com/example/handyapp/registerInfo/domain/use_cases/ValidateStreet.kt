package com.example.handyapp.registerInfo.domain.use_cases

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
