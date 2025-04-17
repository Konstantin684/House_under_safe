package com.example.house_under_safe.ui.home

import com.example.house_under_safe.R

data class HomeItem(
    val planResId: Int,
    val numberPolice: Int,
    val locationRegion: String,
    val typeRealEstate: String,
    val adresRealEstate: String,
    val validatyPeriod: String,
    val risks: List<RiskType>
)

enum class RiskType(val label: String, val iconRes: Int) {
    FIRE("пожар", R.drawable.fire),
    FLOOD("залив", R.drawable.water),
    ELEMENT("стихийное\nбедствие", R.drawable.stihia),
    VANDALISM("вандализм", R.drawable.vandalism),
    ROBBERY("грабеж", R.drawable.grabej)
}

val mockHomeItems = listOf(
     HomeItem(
        planResId = R.drawable.image_plan,
        numberPolice = 123456789,
        locationRegion = "Москва",
        typeRealEstate = "Квартира",
        adresRealEstate = "ул. Примерная, 1",
        validatyPeriod = "01.01.2024 - 01.01.2025",
        risks = listOf(RiskType.FIRE, RiskType.FLOOD, RiskType.ELEMENT, RiskType.VANDALISM)
    ),
    HomeItem(
        planResId = R.drawable.image_plan,
        numberPolice = 987654321,
        locationRegion = "СПБ",
        typeRealEstate = "Дом",
        adresRealEstate = "ул. Вторая, 22",
        validatyPeriod = "02.01.2024 - 02.01.2025",
        risks = listOf(RiskType.VANDALISM, RiskType.ROBBERY)
    ),
    HomeItem(
        planResId = R.drawable.image_plan,
        numberPolice = 165414321,
        locationRegion = "Москва",
        typeRealEstate = "Апартаменты",
        adresRealEstate = "ул. Вишневая, 43",
        validatyPeriod = "07.06.2024 - 07.06.2025",
        risks = listOf(RiskType.FIRE, RiskType.FLOOD, RiskType.ELEMENT, RiskType.VANDALISM, RiskType.ROBBERY)
    ),
    HomeItem(
        planResId = R.drawable.image_plan,
        numberPolice = 161641984,
        locationRegion = "Москва",
        typeRealEstate = "Апартаменты",
        adresRealEstate = "ул. Вишневая, 43",
        validatyPeriod = "07.06.2024 - 07.06.2025",
        risks = listOf(RiskType.FIRE, RiskType.FLOOD, RiskType.ELEMENT, RiskType.VANDALISM, RiskType.ROBBERY)
    ),
    HomeItem(
        planResId = R.drawable.image_plan,
        numberPolice = 984901921,
        locationRegion = "Москва",
        typeRealEstate = "Апартаменты",
        adresRealEstate = "ул. Вишневая, 43",
        validatyPeriod = "07.06.2024 - 07.06.2025",
        risks = listOf(RiskType.FIRE, RiskType.FLOOD, RiskType.ELEMENT, RiskType.VANDALISM, RiskType.ROBBERY)
    )
)

 //val mockHomeItems = listOf<HomeItem>()