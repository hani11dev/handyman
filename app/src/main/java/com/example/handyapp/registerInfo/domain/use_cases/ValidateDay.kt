package com.example.handyapp.registerInfo.domain.use_cases

import com.example.handyapp.register.domain.use_case.ValidationResult


class ValidateDay {
    fun excute(day: Int): ValidationResult {
        if(day < 1 || day > 31){
            return ValidationResult(
                successful = false,
                errorMessage = "incorrect day"
            )

        }
        if(day.toString().isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "empty day"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}