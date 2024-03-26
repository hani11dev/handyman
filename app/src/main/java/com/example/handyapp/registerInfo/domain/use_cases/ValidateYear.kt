package com.example.handyapp.registerInfo.domain.use_cases

import com.example.handyapp.register.domain.use_case.ValidationResult


class ValidateYear {
    fun excute(year: Int): ValidationResult {
        if(year.toString().isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "year is empty"
            )
        }
        if(year > 2005){
            return ValidationResult(
                successful = false,
                errorMessage = "You are too yong"
            )

        }
        return ValidationResult(
            successful = true
        )
    }
}