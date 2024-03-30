package com.example.handyapp.finalRegister

import com.example.handyapp.register.domain.use_case.ValidationResult

class ValidateAverageSalary {
    fun excute(averageSalary: String): ValidationResult {
        if(averageSalary.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "average salary is empty"
            )

        }
        for(item in averageSalary){
            if(!(item.isDigit())){
                return ValidationResult(
                    successful = false,
                    errorMessage = "Invalide average salary"
                )

            }
        }

        if(averageSalary.toInt() < 0){
            return ValidationResult(
                successful = false,
                errorMessage = "Invalide average salary"
            )

        }
        return ValidationResult(
            successful = true
        )
    }
}


