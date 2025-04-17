package com.example.house_under_safe.ui.payments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.house_under_safe.ui.payments.history.PaymentsHistoryFragment
import com.example.house_under_safe.ui.payments.to_pay.PaymentsToPayFragment


class PaymentsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PaymentsToPayFragment()
            1 -> PaymentsHistoryFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}