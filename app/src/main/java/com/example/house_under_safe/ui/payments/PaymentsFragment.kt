package com.example.house_under_safe.ui.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.house_under_safe.R

class PaymentsFragment : Fragment() {

    private lateinit var underlineToPay: View
    private lateinit var underlineHistory: View
    private lateinit var viewPager: ViewPager2
    private lateinit var tabToPay: View
    private lateinit var tabHistory: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_payments, container, false)

        // Инициализация
        underlineToPay = view.findViewById(R.id.underline_to_pay)
        underlineHistory = view.findViewById(R.id.underline_history)
        viewPager = view.findViewById(R.id.viewPagerPayments)
        tabToPay = view.findViewById(R.id.tab_to_pay)
        tabHistory = view.findViewById(R.id.tab_history)

        // Настройка ViewPager2
        viewPager.adapter = PaymentsPagerAdapter(this)
        viewPager.currentItem = 0 // начальный экран
        viewPager.getChildAt(0)?.overScrollMode = View.OVER_SCROLL_NEVER

        // Слушатели вкладок
        tabToPay.setOnClickListener { viewPager.currentItem = 0 }
        tabHistory.setOnClickListener { viewPager.currentItem = 1 }

        // Переключение подчеркивания
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateUnderline(position)
            }
        })

        updateUnderline(0) // стартовое состояние

        return view
    }

    private fun updateUnderline(position: Int) {
        underlineToPay.visibility = if (position == 0) View.VISIBLE else View.INVISIBLE
        underlineHistory.visibility = if (position == 1) View.VISIBLE else View.INVISIBLE
    }
}
