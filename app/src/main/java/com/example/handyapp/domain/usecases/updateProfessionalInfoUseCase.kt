package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class updateProfessionalInfoUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    operator fun invoke(map : HashMap<String , String>) = fireStoreRepository.updateProfessionalInfo(map)
}