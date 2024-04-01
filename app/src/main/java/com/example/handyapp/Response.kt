package com.example.handyapp

sealed class Response<out T> {
    object onLoading : Response<Nothing>()
    data class onSuccess<T>(
        val data: T
    ) : Response<T>()

    data class onFaillure<T>(
        val message : String
    ) : Response<T>()
}