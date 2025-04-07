package com.example.house_under_safe.ui.payments

data class PaymentItem(
    val number: String,
    val policy: String,
    val status: String,
    val amount: String,
    val datetime: String? = null // null → "К оплате", не null → "История"
)
