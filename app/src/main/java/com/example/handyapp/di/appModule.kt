package com.example.handyapp.di

import com.example.handyapp.home.Settings.AuthRepo
import com.example.handyapp.home.Settings.data.AuthRepoImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object appModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provdeAuthRepo(auth : FirebaseAuth) : AuthRepo = AuthRepoImpl(auth)
}