package com.example.house_under_safe.model

data class InsurerInfo(
    val passportVydan: String,      //Паспорт выдан
    val dateVydachi: String,        //Дата выдачи
    val kodPodrazdel: String,       //Код подразделения
    val passportNumber: String?,    //Серия и номер паспорта
    val name: String,               //Имя
    val lastname: String,           //Фамилия
    val otchestvo: String,          //Отчество
    val dateBirth: String,          //Дата рождения
    val placeBirth: String,         //Место рождения
    val adressRegistry: String,     //Адрес регистрации
    val adressLive: String,         //Адрес проживания

)
