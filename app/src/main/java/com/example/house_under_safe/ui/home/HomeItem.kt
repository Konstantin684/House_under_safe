package com.example.house_under_safe.ui.home

import com.example.house_under_safe.R
import com.example.house_under_safe.model.InsuranceRisk
import com.example.house_under_safe.model.PropertyType

data class HomeItemUiModel(
    val planResId: Int,                        // ID ресурса плана (иконки/изображения)
    val policyNumber: String,                  // Номер полиса
    val region: String,                        // Регион расположения
    val propertyType: PropertyType,            // Тип недвижимости (используем enum))
    val subtype: Enum<*>,                      // Подтип недвижимости
    val address: String,                       // Адрес недвижимости
    val period: String,                        // Срок действия, например: "01.01.2024 – 01.01.2025"
    val risks: List<InsuranceRiskUi>           // Список рисков из enum
)

data class InsuranceRiskUi(
    val risk: InsuranceRisk,
    val iconRes: Int
)

val insuranceRiskIcons = mapOf(
    InsuranceRisk.FIRE to R.drawable.fire,
    InsuranceRisk.FLOOD to R.drawable.water,
    InsuranceRisk.ELEMENT to R.drawable.stihia,
    InsuranceRisk.VANDALISM to R.drawable.vandalism,
    InsuranceRisk.ROBBERY to R.drawable.grabej
)

// Для отображения
fun List<InsuranceRisk>.toUiList(): List<InsuranceRiskUi> =
    map { InsuranceRiskUi(it, insuranceRiskIcons[it] ?: R.drawable.unchecked_profile) }

val mockHomeItems = listOf(
    HomeItemUiModel(
        planResId = R.drawable.image_plan,
        policyNumber = "123456789",
        region = "Москва",
        propertyType = PropertyType.CityResidential,
        subtype = PropertyType.CityResidential.Subtype.KVARTIRA,
        address = "ул. Примерная, 1",
        period = "01.01.2024 - 01.01.2025",
        risks = listOf(
            InsuranceRisk.FIRE,
            InsuranceRisk.FLOOD,
            InsuranceRisk.ELEMENT,
            InsuranceRisk.VANDALISM
        ).toUiList()
    ),
    HomeItemUiModel(
        planResId = R.drawable.image_plan,
        policyNumber = "987654321",
        region = "Санкт-Петербург",
        propertyType = PropertyType.CountryResidential,
        subtype = PropertyType.CountryResidential.Subtype.KOTTEDG,
        address = "ул. Вторая, 22",
        period = "02.01.2024 - 02.01.2025",
        risks = listOf(
            InsuranceRisk.VANDALISM,
            InsuranceRisk.ROBBERY
        ).toUiList()
    ),
    HomeItemUiModel(
        planResId = R.drawable.image_plan,
        policyNumber = "165414321",
        region = "Москва",
        propertyType = PropertyType.Commercial,
        subtype = PropertyType.Commercial.Subtype.OFFICE_ROOM,
        address = "ул. Вишневая, 43",
        period = "07.06.2024 - 07.06.2025",
        risks = listOf(
            InsuranceRisk.FIRE,
            InsuranceRisk.FLOOD,
            InsuranceRisk.ELEMENT,
            InsuranceRisk.VANDALISM,
            InsuranceRisk.ROBBERY
        ).toUiList()
    )
    // и т.д.
)





 //val mockHomeItems = listOf<HomeItemUiModel>()