package com.example.handyapp.di

import com.example.handyapp.data.AuthRepositoryImpl
import com.example.handyapp.data.FireStoreRepositoryImpl
import com.example.handyapp.domain.usecases.repository.AuthRepository
import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import com.example.handyapp.home.Settings.AuthRepo
import com.example.handyapp.home.Settings.data.AuthRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provdeAuthRepo(auth : FirebaseAuth) : AuthRepo = AuthRepoImpl(auth)

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository {
        return AuthRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        auth: FirebaseAuth
    ): FireStoreRepository = FireStoreRepositoryImpl(fireStore = firestore, storage = storage ,auth )

}