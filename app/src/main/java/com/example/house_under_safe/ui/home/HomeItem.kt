package com.example.house_under_safe.ui.home

import com.example.house_under_safe.R
import com.example.house_under_safe.model.InsuranceRisk
import com.example.house_under_safe.model.PropertyType

data class HomeItemUiModel(
    val planResId: Int,                        // ID ресурса плана (иконки/изображения)
    val policyNumber: String,                  // Номер полиса
    val region: String,                        // Регион расположения
    val propertyType: PropertyType,              // Тип недвижимости (используем enum))
    val address: String,                       // Адрес недвижимости
    val period: String,                        // Срок действия, например: "01.01.2024 – 01.01.2025"
    val risks: List<InsuranceRisk>             // Список рисков из enum
)

val mockHomeItems = listOf(
    HomeItemUiModel(
        planResId = R.drawable.image_plan,
        policyNumber = "123456789",
        region = "Москва",
        propertyType = PropertyType.CITY_RESIDENTIAL,
        address = "ул. Примерная, 1",
        period = "01.01.2024 - 01.01.2025",
        risks = listOf(
            InsuranceRisk.FIRE,
            InsuranceRisk.FLOOD,
            InsuranceRisk.ELEMENT,
            InsuranceRisk.VANDALISM
        )
    ),
    HomeItemUiModel(
        planResId = R.drawable.image_plan,
        policyNumber = "987654321",
        region = "Санкт-Петербург",
        propertyType = PropertyType.COUNTRY_RESIDENTIAL,
        address = "ул. Вторая, 22",
        period = "02.01.2024 - 02.01.2025",
        risks = listOf(
            InsuranceRisk.VANDALISM,
            InsuranceRisk.ROBBERY
        )
    ),
    HomeItemUiModel(
        planResId = R.drawable.image_plan,
        policyNumber = "165414321",
        region = "Москва",
        propertyType = PropertyType.COMMERCIAL,
        address = "ул. Вишневая, 43",
        period = "07.06.2024 - 07.06.2025",
        risks = listOf(
            InsuranceRisk.FIRE,
            InsuranceRisk.FLOOD,
            InsuranceRisk.ELEMENT,
            InsuranceRisk.VANDALISM,
            InsuranceRisk.ROBBERY
        )
    ),
    HomeItemUiModel(
        planResId = R.drawable.image_plan,
        policyNumber = "161641984",
        region = "Москва",
        propertyType = PropertyType.COMMERCIAL,
        address = "ул. Вишневая, 43",
        period = "07.06.2024 - 07.06.2025",
        risks = listOf(
            InsuranceRisk.FIRE,
            InsuranceRisk.FLOOD,
            InsuranceRisk.ELEMENT,
            InsuranceRisk.VANDALISM,
            InsuranceRisk.ROBBERY
        )
    ),
    HomeItemUiModel(
        planResId = R.drawable.image_plan,
        policyNumber = "984901921",
        region = "Москва",
        propertyType = PropertyType.COMMERCIAL,
        address = "ул. Вишневая, 43",
        period = "07.06.2024 - 07.06.2025",
        risks = listOf(
            InsuranceRisk.FIRE,
            InsuranceRisk.FLOOD,
            InsuranceRisk.ELEMENT,
            InsuranceRisk.VANDALISM,
            InsuranceRisk.ROBBERY
        )
    )
)

 //val mockHomeItems = listOf<HomeItem>()