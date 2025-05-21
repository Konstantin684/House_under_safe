package com.example.house_under_safe.registration

data class RegisterRequest(
    val surname: String,
    val name: String,
    val patronymic: String,
    val mobile_phone: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val token: String
)
