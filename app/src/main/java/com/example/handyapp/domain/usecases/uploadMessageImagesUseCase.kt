package com.example.handyapp.domain.usecases

import android.net.Uri
import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class uploadMessageImagesUseCase @Inject constructor(val fireStoreRepository: FireStoreRepository) {
    operator fun invoke(id : String , images : List<Uri>) = fireStoreRepository.uploadMessageImages(id ,images)
}