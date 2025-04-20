package com.example.house_under_safe.model

import java.io.Serializable

data class PropertyInfo(
    val propertyType: PropertyType,     // Тип недвижимости
    val subCategory: Enum<*>,           // Подтип едвижимости
    val region: String,                 //Регион недвижимости
    val address: String,                // Адрес недвижимости
    val cadastralNumber: String?,       // Кадастровый номер (опционально)
    val totalArea: Double,              // Общая площадь (в м²)
    val floor: Int?,                    // Этаж (если применимо)
    val floorsTotal: Int?,              // Общее количество этажей (если применимо)
    val yearBuilt: Int?                 // Год постройки (если известен)
)

sealed class PropertyType(val label: String) : Serializable {
    object CityResidential : PropertyType("Городская жилая недвижимость") {
        enum class Subtype(val label: String) {
            KVARTIRA("Квартира"),
            KOMNATA_KOM_KVARTIRA("Комната в коммунальной квартире")
        }
    }

    object CountryResidential : PropertyType("Загородная жилая недвижимость") {
        enum class Subtype(val label: String) {
            DACHNIY_DOM("Дачный дом"),
            KOTTEDG("Загородный дом")
        }
    }

    object CountryNotResidential : PropertyType("Загородная нежилая недвижимость") {
        enum class Subtype(val label: String) {
            APARTAMENTS("Апартаменты"),
            GARAGE("Гараж")
        }
    }

    object Commercial : PropertyType("Коммерческая недвижимость") {
        enum class Subtype(val label: String) {
            SKLAD("Склад"),
            OFFICE_ROOM("Офисные помещения"),
            GOSTINICA("Гостиница"),
            HOTEL("Отель"),
            MOTEL("Мотель"),
            RETAIL_PREMISE("Торговые помещения")
        }
    }

    object Industrial : PropertyType("Промышленная недвижимость") {
        enum class Subtype(val label: String) {
            INDUSTRIAL_FACILITIES("Промышленные сооружения"),
            ZAVOD("Завод"),
            FABRICK("Фабрика")
        }
    }

    object ProdleniePolisa: PropertyType("Продление полиса") {
        enum class Subtype(val label:String){

        }
    }
}
