package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.AuthRepository
import javax.inject.Inject

class signUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(email : String , password : String , phoneNumber : String) = authRepository.signUp(email, password, phoneNumber)
}