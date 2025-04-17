package com.example.house_under_safe.model

data class InsuranceConditions(
    val insuredAmount: Double,     // страховая сумма
    val premium: Double,           // страховая премия
    val paymentFrequency: PaymentFrequency,
    val deductible: Double         // франшиза
)

enum class PaymentFrequency {
    ONE_TIME, MONTHLY, QUARTERLY, YEARLY
}
