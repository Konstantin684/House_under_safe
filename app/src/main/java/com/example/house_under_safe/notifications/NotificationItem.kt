package com.example.house_under_safe.notifications

data class NotificationItem(
    val type: NotificationType,
    val title: String,
    val message: String,
    val date: String
)

enum class NotificationType {
    ALL, PAYMENT, CLAIM
}