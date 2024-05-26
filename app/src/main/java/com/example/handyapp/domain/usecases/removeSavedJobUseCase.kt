package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class removeSavedJobUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    operator fun invoke(jobID : String) = fireStoreRepository.removeJob(jobID)
}