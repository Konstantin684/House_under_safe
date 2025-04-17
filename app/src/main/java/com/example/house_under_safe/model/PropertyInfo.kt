package com.example.house_under_safe.model

data class PropertyInfo(
    val propertyType: PropertyType,     // Тип недвижимости
    val address: String,                // Адрес недвижимости
    val cadastralNumber: String?,       // Кадастровый номер (опционально)
    val totalArea: Double,              // Общая площадь (в м²)
    val floor: Int?,                    // Этаж (если применимо)
    val floorsTotal: Int?,              // Общее количество этажей (если применимо)
    val yearBuilt: Int?                 // Год постройки (если известен)
)

enum class PropertyType(val label: String) {
    CITY_RESIDENTIAL("Городская жилая недвижимость"),
    COUNTRY_RESIDENTIAL("Загородная жилая недвижимость"),
    CITY_COMMERCIAL("Городская нежилая недвижимость"),
    COMMERCIAL("Коммерческая недвижимость"),
    INDUSTRIAL("Промышленная недвижимость")
}

