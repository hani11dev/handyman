package com.example.handyapp.finalRegister

import com.example.handyapp.register.domain.use_case.ValidationResult

class ValidateWilaya {
    fun excute(wilaya: String): ValidationResult{
        if(wilaya.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "wilaya is empty"
            )

        }
        return ValidationResult(
            successful = true
        )
    }
}
