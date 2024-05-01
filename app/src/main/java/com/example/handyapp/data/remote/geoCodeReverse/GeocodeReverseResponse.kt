package com.koDea.fixMasterClient.data.remote.dto.geoCodeReverse

import androidx.annotation.Keep
import com.koDea.fixMasterClient.data.remote.dto.geoCodeReverse.Address

@Keep
data class GeocodeReverseResponse(
    val address: Address? =null,
    val boundingbox: List<String?>? = null,
    val display_name: String?  = null,
    val lat: String? = null,
    val licence: String? = null,
    val lon: String? = null,
    val osm_id: Long? = null,
    val osm_type: String? = null,
    val place_id: Int? = null
)