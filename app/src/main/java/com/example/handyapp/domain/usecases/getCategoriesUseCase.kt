package com.example.handyapp.domain.usecases

import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import javax.inject.Inject


class getCategoriesUseCase @Inject constructor (private val repository: FireStoreRepository){
    operator fun invoke() = repository.getCategories()

}