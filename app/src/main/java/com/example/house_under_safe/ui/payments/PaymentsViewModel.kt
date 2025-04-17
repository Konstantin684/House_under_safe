package com.example.house_under_safe.ui.payments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentsViewModel : ViewModel() {


    private val _payments = MutableLiveData<MutableList<PaymentItem>>(mockPaymentItems.toMutableList())
    val payments: LiveData<MutableList<PaymentItem>> get() = _payments

    fun updatePayment(updated: PaymentItem) {
        val list = _payments.value ?: return
        val index = list.indexOfFirst { it.number == updated.number }
        if (index != -1) {
            list[index] = updated
            _payments.value = list.toMutableList() // триггер перерисовки
        }
    }
}