package com.example.house_under_safe.ui.payments

data class PaymentItem(
    val number: String,
    val policy: String,
    val status: String,
    val amount: String,
    val datetime: String? = null // null → "К оплате", не null → "История"
)


val mockPaymentItems = listOf(
    // Платежи "К оплате"
    PaymentItem(
        number = "001",
        policy = "Полис №123456",
        status = "Ожидает оплаты",
        amount = "1 200 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "002",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "003",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "004",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "005",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "006",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "007",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "008",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "009",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),
    PaymentItem(
        number = "010",
        policy = "Полис №789012",
        status = "Просрочен",
        amount = "2 500 ₽",
        datetime = null
    ),

    // История оплат
    PaymentItem(
        number = "011",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    ),
    PaymentItem(
        number = "012",
        policy = "Полис №901234",
        status = "Оплачен",
        amount = "3 000 ₽",
        datetime = "10.04.2025 09:20"
    ),
    PaymentItem(
        number = "013",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    ),
    PaymentItem(
        number = "014",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    ),
    PaymentItem(
        number = "015",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    ),
    PaymentItem(
        number = "016",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    ),
    PaymentItem(
        number = "017",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    ),
    PaymentItem(
        number = "018",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    ),
    PaymentItem(
        number = "019",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    ),
    PaymentItem(
        number = "020",
        policy = "Полис №345678",
        status = "Оплачен",
        amount = "1 800 ₽",
        datetime = "14.04.2025 14:33"
    )
)
