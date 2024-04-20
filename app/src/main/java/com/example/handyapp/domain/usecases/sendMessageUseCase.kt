package com.example.handyapp.domain.usecases

import android.net.Uri
import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import com.example.handyapp.home.chat.Message
import javax.inject.Inject

class sendMessageUseCase @Inject constructor(val fireStoreRepository: FireStoreRepository) {
    operator fun invoke(message: Message, images : List<Uri>) = fireStoreRepository.sendMessage(message , images)
}