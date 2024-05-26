package com.example.handyapp.domain.usecases

import android.net.Uri
import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class updateRegisterInfoUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    operator fun invoke(firstName: String,
                        lastName: String,
                        day: String,
                        month: String,
                        year: String,imageUri : Uri , fileUri : Uri) = fireStoreRepository.registerInfo(firstName, lastName, day, month, year , imageUri , fileUri)
}