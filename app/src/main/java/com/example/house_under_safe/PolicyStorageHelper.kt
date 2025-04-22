package com.example.house_under_safe

import android.content.Context
import android.util.Log
import com.example.house_under_safe.model.*
import com.example.house_under_safe.ui.home.HomeItemUiModel
import com.example.house_under_safe.ui.home.InsuranceRiskUi
import com.example.house_under_safe.ui.home.insuranceRiskIcons
import com.example.house_under_safe.util.PropertySubtypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

object PolicyStorageHelper {
    private const val FILE_NAME = "full_policies.json"

    fun saveFullPolicy(context: Context, policy: InsurancePolicy) {
        val policies = loadFullPolicies(context).toMutableList()
        policies.add(policy)

        val gson = createGson()
        val json = gson.toJson(policies)

        val file = File(context.filesDir, "full_policies.json")
        context.openFileOutput(file.name, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }

        Log.d("PolicyStorageHelper", "JSON-файл создан: ${file.absolutePath}")
        Log.d("PolicyStorageHelper", "Содержимое JSON:\n$json")
    }


    fun loadFullPolicies(context: Context): List<InsurancePolicy> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            Log.d("PolicyStorageHelper", "Файл $FILE_NAME не существует. Возвращается пустой список.")
            return emptyList()
        }
        return try {
            val json = file.bufferedReader().use { it.readText() }
            Log.d("PolicyStorageHelper", "Загруженный JSON:\n$json")
            createGson().fromJson(json, object : TypeToken<List<InsurancePolicy>>() {}.type)
        } catch (e: Exception) {
            Log.e("PolicyStorageHelper", "Ошибка загрузки InsurancePolicy", e)
            emptyList()
        }
    }


    fun deleteAllPolicies(context: Context) {
        val file = File(context.filesDir, FILE_NAME)
        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                Log.d("PolicyStorageHelper", "Файл $FILE_NAME успешно удалён.")
            } else {
                Log.e("PolicyStorageHelper", "Не удалось удалить файл $FILE_NAME.")
            }
        } else {
            Log.d("PolicyStorageHelper", "Файл $FILE_NAME не найден для удаления.")
        }
    }


    fun loadHomeItems(context: Context): List<HomeItemUiModel> {
        return loadFullPolicies(context).map { policy ->
            val planResId = when (policy.property.propertyType) {
                is PropertyType.CityResidential -> R.drawable.gor_jil_ned
                is PropertyType.CountryResidential -> R.drawable.zagor_jil_ned
                is PropertyType.CountryNotResidential -> R.drawable.gor_nejil_ned
                is PropertyType.Commercial -> R.drawable.komer_ned
                is PropertyType.Industrial -> R.drawable.prom_ned
                is PropertyType.ProdleniePolisa -> R.drawable.unchecked_profile
            }

            HomeItemUiModel(
                planResId = planResId,
                policyNumber = policy.policyNumber,
                region = policy.property.region,
                propertyType = policy.property.propertyType,
                subtype = policy.property.subCategory,
                address = policy.property.address,
                period = "${policy.startDate} – ${policy.endDate}",
                risks = policy.risks.map { InsuranceRiskUi(it, insuranceRiskIcons[it] ?: R.drawable.unchecked_profile) }
            )
        }
    }

    private fun createGson(): Gson {
        val propertyTypeAdapter = RuntimeTypeAdapterFactory
            .of(PropertyType::class.java, "type")
            .registerSubtype(PropertyType.CityResidential::class.java, "CityResidential")
            .registerSubtype(PropertyType.CountryResidential::class.java, "CountryResidential")
            .registerSubtype(PropertyType.CountryNotResidential::class.java, "CountryNotResidential")
            .registerSubtype(PropertyType.Commercial::class.java, "Commercial")
            .registerSubtype(PropertyType.Industrial::class.java, "Industrial")
            .registerSubtype(PropertyType.ProdleniePolisa::class.java, "ProdleniePolisa")

        return GsonBuilder()
            .registerTypeAdapterFactory(propertyTypeAdapter)
            .registerTypeAdapter(PropertySubtype::class.java, PropertySubtypeAdapter()) // 👈 ВАЖНО!
            .create()
    }
}
