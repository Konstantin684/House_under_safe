package com.example.house_under_safe.model

data class PaymentEntry(
    val date: String,              // Дата оплаты или выставления счёта (в формате "дд.мм.гггг" или ISO)
    val amount: Double,            // Сумма платежа
    val status: PaymentStatus,     // Статус платежа (оплачен, ожидается, просрочен)
    val transactionId: String?     // ID транзакции (если доступен)
)

enum class PaymentStatus {
    PAID,     // Оплачен
    PENDING,  // В ожидании оплаты
    OVERDUE   // Просрочен
}