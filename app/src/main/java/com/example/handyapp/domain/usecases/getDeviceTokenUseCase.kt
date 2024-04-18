package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class getDeviceTokenUseCase @Inject constructor(val fireStoreRepository: FireStoreRepository) {
    operator fun invoke(id : String) = fireStoreRepository.getDeviceToken(id)
}