package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.model.Notification
import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class sendNotificationFirestoreUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    operator fun invoke(notification : Notification) = fireStoreRepository.sendNotificationFireStore(notification)
}