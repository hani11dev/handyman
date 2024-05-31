package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject

class calcRating @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    operator fun invoke() = fireStoreRepository.calculeRating()
}