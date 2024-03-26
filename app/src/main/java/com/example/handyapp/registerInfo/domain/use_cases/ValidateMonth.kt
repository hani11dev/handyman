package com.example.handyapp.registerInfo.domain.use_cases

import com.example.handyapp.register.domain.use_case.ValidationResult

class ValidateMonth {
    fun excute(month: Int): ValidationResult {
        if(month.toString().isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "empty month"
            )
        }
        if(month > 12 || month < 1){
            return ValidationResult(
                successful = false,
                errorMessage = "invalid month"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}
