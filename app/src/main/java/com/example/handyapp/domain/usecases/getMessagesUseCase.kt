package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class getMessagesUseCase @Inject constructor(val fireStoreRepository: FireStoreRepository){
    operator fun invoke(receiver : String) = fireStoreRepository.getMessages(receiver)
}