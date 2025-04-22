package com.example.house_under_safe.ui.home

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.house_under_safe.R
import com.example.house_under_safe.model.InsuranceRisk
import com.example.house_under_safe.model.PropertySubtype
import com.example.house_under_safe.model.PropertyType

@Parcelize
data class HomeItemUiModel(
    val planResId: Int,
    val policyNumber: String,
    val region: String,
    val propertyType: PropertyType,
    val subtype: PropertySubtype,  // ✅ Теперь это корректный тип
    val address: String,
    val period: String,
    val risks: List<InsuranceRiskUi>
) : Parcelable


@Parcelize
data class InsuranceRiskUi(
    val risk: InsuranceRisk,
    val iconRes: Int
) : Parcelable

val insuranceRiskIcons = mapOf(
    InsuranceRisk.FIRE to R.drawable.fire,
    InsuranceRisk.FLOOD to R.drawable.water,
    InsuranceRisk.ELEMENT to R.drawable.stihia,
    InsuranceRisk.VANDALISM to R.drawable.vandalism,
    InsuranceRisk.ROBBERY to R.drawable.grabej
)

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
)
