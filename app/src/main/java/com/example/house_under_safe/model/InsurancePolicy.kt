package com.example.house_under_safe.model

import com.example.house_under_safe.R

data class InsurancePolicy(
    val policyNumber: String,              // Номер полиса
    val issueDate: String,                 // Дата оформления
    val startDate: String,                 // Дата начала действия
    val endDate: String,                   // Дата окончания действия
    val status: PolicyStatus,              // Статус полиса (активен, истек и т.д.)
    val insuranceType: InsuranceType,      // Тип страхования
    val insurer: InsurerInfo,              // Данные страхователя
    val property: PropertyInfo,            // Данные о недвижимости
    val conditions: InsuranceConditions,   // Условия страхования
    val risks: List<InsuranceRisk>,        // Перечень покрываемых рисков
    val documents: List<PolicyDocument>,   // Приложенные документы
    val payments: List<PaymentEntry>       // Платежи по полису
)

enum class PolicyStatus {
    ACTIVE,     // активный
    EXPIRED,    // срок действия истёк
    SUSPENDED,  // временно приостановлен
    CANCELLED   // отменён
}

enum class InsuranceType {
    PROPERTY,   // имущество
    MORTGAGE,   // ипотека
    TITLE       // право собственности
}

enum class InsuranceRisk (val label: String, val iconRes: Int) {
    FIRE("пожар", R.drawable.fire),
    FLOOD("залив", R.drawable.water),
    ELEMENT("стихийное\nбедствие", R.drawable.stihia),
    VANDALISM("вандализм", R.drawable.vandalism),
    ROBBERY("грабеж", R.drawable.grabej)
}
