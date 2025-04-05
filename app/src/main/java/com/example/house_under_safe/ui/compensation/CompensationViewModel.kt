package com.example.house_under_safe.ui.compensation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompensationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is compensation Fragment"
    }
    val text: LiveData<String> = _text
}