package com.example.handyapp.data.remote

import com.koDea.fixMasterClient.data.remote.dto.geoCodeReverse.GeocodeReverseResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface GeocodeReverseApi {
    @GET("reverse")
    suspend fun getLocationInfo(
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("api_key") api_key : String = "65da298808052629177612scj12fc88",
    ): GeocodeReverseResponse
}

//https://geocode.maps.co/reverse?lat=36.503397&lon=2.876533&api_key=65da298808052629177612scj12fc88