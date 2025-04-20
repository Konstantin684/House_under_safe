package com.example.house_under_safe.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.house_under_safe.ui.home.HomeItemUiModel


class MainViewModel : ViewModel() {

    // Закрытый список полисов
    private val _policies = MutableLiveData<List<HomeItemUiModel>>(emptyList())

    // Публичный доступ к LiveData
    val policies: LiveData<List<HomeItemUiModel>> = _policies

    // Метод добавления нового полиса
    fun addPolicy(policy: HomeItemUiModel) {
        val updatedList = _policies.value.orEmpty().toMutableList()
        updatedList.add(policy)
        _policies.value = updatedList
    }

    // Метод очистки (по желанию)
    fun clearPolicies() {
        _policies.value = emptyList()
    }
}
