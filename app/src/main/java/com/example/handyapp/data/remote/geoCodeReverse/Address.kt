package com.koDea.fixMasterClient.data.remote.dto.geoCodeReverse


import com.google.gson.annotations.SerializedName

@androidx.annotation.Keep
data class Address(
    @SerializedName(value = "ISO3166-2-lvl4")
    val ISO3166_2_lvl4: String? = null,
    val amenity: String? = null,
    val country: String? = null,
    val country_code: String? = null,
    val county: String? = null,
    val postcode: String? = null,
    val road: String? = null,
    val state: String? = null,
    val town: String? = null
)