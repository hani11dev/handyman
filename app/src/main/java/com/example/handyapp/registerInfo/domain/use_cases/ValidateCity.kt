import com.example.handyapp.register.domain.use_case.ValidationResult

class ValidateCity {
    fun excute(city: String): ValidationResult {
        if(city.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "city is empty"
            )

        }
        return ValidationResult(
            successful = true
        )
    }
}


