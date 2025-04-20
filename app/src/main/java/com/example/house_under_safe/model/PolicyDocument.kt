package com.example.house_under_safe.model

data class PolicyDocument(
    val name: String,              // Название документа (например: "Полис", "Фото объекта")
    val type: DocumentType,        // Тип документа
    val url: String                // Ссылка на файл (может быть локальной или удалённой)
)

enum class DocumentType {
    POLICY_PDF,         // Страховой полис в формате PDF
    PHOTO               // Фотография объекта недвижимости
}