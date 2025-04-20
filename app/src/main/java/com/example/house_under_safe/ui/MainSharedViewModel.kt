package com.example.house_under_safe.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.house_under_safe.ui.home.HomeItemUiModel

class MainSharedViewModel(application: Application) : AndroidViewModel(application) {

    private val _policies = MutableLiveData<List<HomeItemUiModel>>(emptyList())
    val policies: LiveData<List<HomeItemUiModel>> = _policies

    fun addPolicy(policy: HomeItemUiModel) {
        val updatedList = _policies.value.orEmpty().toMutableList().apply {
            add(policy)
        }
        _policies.value = updatedList
    }

    fun clearPolicies() {
        _policies.value = emptyList()
    }
}