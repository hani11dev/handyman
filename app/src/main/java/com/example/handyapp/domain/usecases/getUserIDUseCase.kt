package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.AuthRepository
import javax.inject.Inject

class getUserIDUseCase @Inject constructor(val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.getUserID()
}