package com.example.handyapp.registerInfo.domain.use_cases

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
