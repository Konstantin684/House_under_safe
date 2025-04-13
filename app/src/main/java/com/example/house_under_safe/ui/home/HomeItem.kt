package com.example.house_under_safe.ui.home

data class HomeItem(
    val planResId: Int,
    val numberPolice: Int,
    val locationRegion: String,
    val typeRealEstate: String,
    val adresRealEstate: String,
    val validatyPeriod: String,
    val risks: List<RiskType>
)

enum class RiskType {
    FIRE, FLOOD, ELEMENT, VANDALISM, ROBBERY
}
