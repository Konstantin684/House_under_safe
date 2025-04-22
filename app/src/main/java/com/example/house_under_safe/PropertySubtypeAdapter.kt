package com.example.house_under_safe.util

import com.example.house_under_safe.model.*
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class PropertySubtypeAdapter : TypeAdapter<PropertySubtype>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: PropertySubtype?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value.label) // сериализация подтипа как строки (например "KVARTIRA")
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): PropertySubtype? {
        val name = reader.nextString()

        // Перебираем все enum-классы, реализующие PropertySubtype
        return PropertyType.CityResidential.Subtype.values().find { it.name == name }
            ?: PropertyType.CountryResidential.Subtype.values().find { it.name == name }
            ?: PropertyType.CountryNotResidential.Subtype.values().find { it.name == name }
            ?: PropertyType.Commercial.Subtype.values().find { it.name == name }
            ?: PropertyType.Industrial.Subtype.values().find { it.name == name }
            ?: throw IllegalArgumentException("Неизвестный PropertySubtype: $name")
    }
}
