package com.example.house_under_safe.model

data class InsurerInfo(
    val fullName: String,         // Полное имя страхователя
    val passportNumber: String?,  // Номер паспорта
    val inn: String?,             // ИНН
    val phone: String,            // Номер телефона
    val email: String,            // Адрес электронной почты
    val address: String           // Почтовый адрес
)
