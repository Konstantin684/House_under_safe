package com.example.house_under_safe

import com.google.gson.*
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type

class RuntimeTypeAdapterFactory<T>(
    private val baseType: Class<T>,
    private val typeFieldName: String,
    private val maintainType: Boolean = false
) : TypeAdapterFactory {

    private val labelToSubtype = mutableMapOf<String, Class<out T>>()
    private val subtypeToLabel = mutableMapOf<Class<out T>, String>()

    fun registerSubtype(
        type: Class<out T>,
        label: String = type.simpleName
    ): RuntimeTypeAdapterFactory<T> {
        require(!labelToSubtype.containsKey(label)) {
            "Types and labels must be unique. Label '$label' is already registered."
        }
        labelToSubtype[label] = type
        subtypeToLabel[type] = label
        return this
    }

    override fun <R> create(gson: Gson, type: TypeToken<R>): TypeAdapter<R>? {
        if (!baseType.isAssignableFrom(type.rawType)) return null

        val labelToDelegate = mutableMapOf<String, TypeAdapter<out T>>()
        val subtypeToDelegate = mutableMapOf<Class<out T>, TypeAdapter<out T>>()

        for ((label, subtype) in labelToSubtype) {
            val subtypeToken = TypeToken.get(subtype)
            val delegate = gson.getDelegateAdapter(this, subtypeToken)
            labelToDelegate[label] = delegate
            subtypeToDelegate[subtype] = delegate
        }

        return object : TypeAdapter<R>() {
            override fun write(out: JsonWriter, value: R) {
                val srcType = value!!::class.java as Class<out T>
                val label = subtypeToLabel[srcType]
                    ?: throw JsonParseException("Cannot serialize ${srcType.name}; did you forget to register a subtype?")
                val delegate = subtypeToDelegate[srcType] as TypeAdapter<R>
                val jsonObject = delegate.toJsonTree(value).asJsonObject

                if (maintainType) {
                    Streams.write(jsonObject, out)
                } else {
                    val clone = JsonObject()
                    clone.add(typeFieldName, JsonPrimitive(label))
                    for ((key, jsonElement) in jsonObject.entrySet()) {
                        clone.add(key, jsonElement)
                    }
                    Streams.write(clone, out)
                }
            }

            override fun read(reader: JsonReader): R {
                val jsonElement = Streams.parse(reader)
                val jsonObject = jsonElement.asJsonObject
                val labelJsonElement = jsonObject.remove(typeFieldName)
                    ?: throw JsonParseException("Cannot deserialize $baseType because it does not define a field named $typeFieldName")

                val label = labelJsonElement.asString
                val subtype = labelToSubtype[label]
                    ?: throw JsonParseException("Cannot deserialize $baseType subtype named '$label'; did you forget to register a subtype?")

                val delegate = labelToDelegate[label] as TypeAdapter<R>
                return delegate.fromJsonTree(jsonObject)
            }
        }
    }

    companion object {
        fun <T> of(baseType: Class<T>, typeFieldName: String): RuntimeTypeAdapterFactory<T> {
            return RuntimeTypeAdapterFactory(baseType, typeFieldName)
        }
    }
}
