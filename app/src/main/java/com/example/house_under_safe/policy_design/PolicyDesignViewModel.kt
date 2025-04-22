package com.example.house_under_safe.policy_design

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.model.InsuranceConditions
import com.example.house_under_safe.model.InsurerInfo
import com.example.house_under_safe.model.PolicyDocument
import com.example.house_under_safe.model.PropertyInfo
import com.example.house_under_safe.model.PropertySubtype
import com.example.house_under_safe.model.PropertyType

class PolicyDesignViewModel : ViewModel() {
    val insurerInfo = MutableLiveData<InsurerInfo?>()
    val propertyInfo = MutableLiveData<PropertyInfo?>()
    val insuranceConditions = MutableLiveData<InsuranceConditions?>()
    val documents = MutableLiveData<List<PolicyDocument>>(emptyList())

    val selectedPropertyType = MutableLiveData<PropertyType?>()
    val selectedSubtype = MutableLiveData<PropertySubtype?>() // ✅ новый тип
    val selectedSubtypeIndex = MutableLiveData<Int?>()

    val policyNumber = MutableLiveData<String>()
    val startDate = MutableLiveData<String>()
    val endDate = MutableLiveData<String>()
}



