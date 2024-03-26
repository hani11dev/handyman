package com.example.handyapp.registerInfo.domain.use_cases

import com.example.handyapp.register.domain.use_case.ValidationResult


class ValidateFirstName {
    fun excute(firstName: String): ValidationResult {
        if(firstName.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "empty first Name"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}