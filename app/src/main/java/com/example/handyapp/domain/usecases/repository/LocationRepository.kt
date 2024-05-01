package com.example.handyapp.domain.usecases.repository

import com.koDea.fixMasterClient.data.remote.dto.geoCodeReverse.GeocodeReverseResponse

interface LocationRepository {
    suspend fun getLoscationInfo(lat : String , lon : String) : GeocodeReverseResponse
}