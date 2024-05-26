package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.AuthRepository
import javax.inject.Inject

class signInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(email : String , password : String) = authRepository.signIn(email , password)
}