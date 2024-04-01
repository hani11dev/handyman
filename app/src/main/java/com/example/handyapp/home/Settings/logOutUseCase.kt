package com.example.handyapp.home.Settings

import javax.inject.Inject

class logOutUseCase @Inject constructor(val authRepo: AuthRepo){
    operator fun invoke() = authRepo.signOut()
}