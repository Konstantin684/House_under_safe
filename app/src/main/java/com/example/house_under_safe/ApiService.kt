package com.example.house_under_safe

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/user/registration")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}
