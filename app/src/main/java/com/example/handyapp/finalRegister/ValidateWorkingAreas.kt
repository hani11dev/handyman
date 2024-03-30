package com.example.handyapp.finalRegister

import com.example.handyapp.register.domain.use_case.ValidationResult

class ValidateWorkingAreas {
    fun excute(workingAreas: String): ValidationResult {
        if(workingAreas.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "this field can't be empty"
            )

        }
        return ValidationResult(
            successful = true
        )
    }
}


