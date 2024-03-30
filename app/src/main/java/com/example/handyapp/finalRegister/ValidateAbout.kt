package com.example.handyapp.finalRegister

import com.example.handyapp.register.domain.use_case.ValidationResult

class ValidateAbout {
    fun excute(about: String): ValidationResult {
        if(about.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Write something about yourself"
            )

        }
        return ValidationResult(
            successful = true
        )
    }
}


