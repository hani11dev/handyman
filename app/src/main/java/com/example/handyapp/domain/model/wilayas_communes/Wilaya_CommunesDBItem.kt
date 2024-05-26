package com.koDea.fixMasterClient.domain.model.wilayas_communes

data class Wilaya_CommunesDBItem(
    val adjacentWilayas: List<Int>,
    val dairats: List<Dairat>,
    val mattricule: Int,
    val name: String,
    val name_ar: String,
    val name_ber: String,
    val name_en: String,
    val phoneCodes: List<Int>,
    val postalCodes: List<Int>
)