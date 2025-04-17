package com.example.house_under_safe.ui.payments

data class PaymentItem(
    val number: String,
    val policy: String,
    val amount: String,
    val datetime: String? = null, // используется только в истории
    val status: PaymentStatus     // статус платежа
)

enum class PaymentStatus {
    AWAITING, // ожидает оплаты
    PAID,     // оплачен
    CANCELED  // отменен
}


//тестовые данные для обоих вкладок
 val mockPaymentItems = listOf(
    PaymentItem("001", "Полис №123456", "1 200 ₽", null, PaymentStatus.AWAITING)
)


/*
//тестовые данные для вкладке "к оплате"
val mockPaymentItems = listOf(
    PaymentItem("001", "Полис №123456", "1 200 ₽", null, PaymentStatus.AWAITING)
)


//тестовые данные для вкладки "история платежей"
val mockPaymentItems = listOf(
    PaymentItem("002", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("003", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("004", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("005", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("006", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("007", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("008", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("009", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("010", "Полис №789012", "2 500 ₽", "14.04.2025 14:33", PaymentStatus.CANCELED),
    PaymentItem("011", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID),
    PaymentItem("012", "Полис №901234", "3 000 ₽", "10.04.2025 09:20", PaymentStatus.PAID),
    PaymentItem("013", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID),
    PaymentItem("014", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID),
    PaymentItem("015", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID),
    PaymentItem("016", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID),
    PaymentItem("017", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID),
    PaymentItem("018", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID),
    PaymentItem("019", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID),
    PaymentItem("020", "Полис №345678", "1 800 ₽", "14.04.2025 14:33", PaymentStatus.PAID)
)
*/


// val mockPaymentItems = listOf<PaymentItem>()