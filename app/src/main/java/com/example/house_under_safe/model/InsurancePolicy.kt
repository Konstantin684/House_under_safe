package com.example.house_under_safe.model

import com.example.house_under_safe.R

data class InsurancePolicy(
    val policyNumber: String,              // Номер полиса
    val issueDate: String,                 // Дата оформления
    val startDate: String,                 // Дата начала действия
    val endDate: String,                   // Дата окончания действия
    val status: PolicyStatus,              // Статус полиса (активен, истек и т.д.)
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
}

enum class InsuranceRisk(val label: String) {
    FIRE("пожар"),
    FLOOD("залив"),
    ELEMENT("стихийное бедствие"),
    VANDALISM("вандализм"),
    ROBBERY("грабеж")
}

data class InsuranceConditions(
    val constructionCost: Double,                 // Конструкция страхования, руб.
    val finishingCost: Double?,                   // Отделка, руб. (необязательно)
    val equipmentCost: Double?,                   // Техническое оборудование, руб. (необязательно)
    val movablePropertyCost: Double?,             // Движимое имущество, руб. (необязательно)
    val civilLiabilityCost: Double?,              // Гражданская ответственность, руб. (необязательно)
    val risks: List<InsuranceRisk>,               // Страховые риски (выбранные)
    val insuranceTermInYears: Int,                // Срок страхования в годах (с SeekBar)
    val paymentFrequency: PaymentFrequency        // Частота платежей (RadioGroup)
)

enum class PaymentFrequency (val label: String){
    ONE_TIME("Один раз"),
    MONTHLY("Ежемесячно"),
    QUARTERLY("Ежеквартально"),
    YEARLY("Ежегодно")
}