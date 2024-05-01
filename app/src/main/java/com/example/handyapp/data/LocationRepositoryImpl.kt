package com.example.handyapp.data

import com.example.handyapp.data.remote.GeocodeReverseApi
import com.example.handyapp.domain.usecases.repository.LocationRepository

import com.koDea.fixMasterClient.data.remote.dto.geoCodeReverse.GeocodeReverseResponse

import javax.inject.Inject


class LocationRepositoryImpl @Inject constructor (private val api : GeocodeReverseApi) :
    LocationRepository {
    override suspend fun getLoscationInfo(lat : String , lon : String) : GeocodeReverseResponse {
        return (api.getLocationInfo(lat, lon))
    }
}
