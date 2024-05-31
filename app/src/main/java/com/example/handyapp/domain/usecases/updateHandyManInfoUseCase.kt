package com.example.handyapp.domain.usecases

import android.net.Uri
import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class updateHandyManInfoUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    operator fun invoke(
        about: String,
        workingAreas: String,
        averageSalary: Double,
        city: String,
        wilaya: String,
        street: String,
        lat: String,
        long: String,
        portfolio : List<Uri>
    ) = fireStoreRepository.updateFinalRegisterInfo(
        about,
        workingAreas,
        averageSalary,
        city,
        wilaya,
        street,
        lat,
        long,
        portfolio
    )
}