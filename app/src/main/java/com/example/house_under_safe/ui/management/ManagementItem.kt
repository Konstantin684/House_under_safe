package com.example.house_under_safe.ui.management

import com.example.house_under_safe.model.PropertyType

data class ManagementItem(
    val iconResId: Int,
    val title: String,
    val description: String,
    val propertyType: PropertyType
)

