package com.example.house_under_safe.ui.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.house_under_safe.R

class PaymentsFragment : Fragment() {

    private lateinit var underlineToPay: View
    private lateinit var underlineHistory: View
    private lateinit var listToPay: ListView
    private lateinit var listHistory: ListView
    private lateinit var placeholderPay: View
    private lateinit var placeholderHistory: View

    private lateinit var toPayItems: List<PaymentItem>
    private lateinit var historyItems: List<PaymentItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_payments, container, false)

        // Вкладки
        val tabToPay = view.findViewById<View>(R.id.tab_to_pay)
        val tabHistory = view.findViewById<View>(R.id.tab_history)
        underlineToPay = view.findViewById(R.id.underline_to_pay)
        underlineHistory = view.findViewById(R.id.underline_history)

        // Списки и плейсхолдеры
        listToPay = view.findViewById(R.id.listToPay)
        listHistory = view.findViewById(R.id.listHistory)
        placeholderPay = view.findViewById(R.id.placeholder_pay)
        placeholderHistory = view.findViewById(R.id.placeholder_history)

        // Пример данных
        toPayItems = emptyList()
        historyItems = emptyList()

        /*
        toPayItems = listOf(
              PaymentItem("123456", "№ 986345205", "ожидает оплаты", "35 000,00 ₽"),
              PaymentItem("987654", "№ 773345998", "ожидает оплаты", "55 000,00 ₽")
        )


        historyItems = listOf(
              PaymentItem("123456", "№ 986345205", "оплачено", "35 000,00 ₽", "19.02.2025 16:28:34"),
              PaymentItem("987654", "№ 773345998", "оплачено", "55 000,00 ₽", "20.02.2025 11:12:45")
        )
        */

        // Установка адаптеров
        listToPay.adapter = PaymentToPayAdapter(requireContext(), toPayItems)
        listHistory.adapter = PaymentHistoryAdapter(requireContext(), historyItems)

        // Слушатели вкладок
        tabToPay.setOnClickListener { switchTab(isToPay = true) }
        tabHistory.setOnClickListener { switchTab(isToPay = false) }

        // Показать начальную вкладку
        switchTab(isToPay = true)

        return view
    }

    private fun switchTab(isToPay: Boolean) {
        underlineToPay.visibility = if (isToPay) View.VISIBLE else View.INVISIBLE
        underlineHistory.visibility = if (!isToPay) View.VISIBLE else View.INVISIBLE

        if (isToPay) {
            if (toPayItems.isEmpty()) {
                listToPay.visibility = View.GONE
                placeholderPay.visibility = View.VISIBLE
            } else {
                listToPay.visibility = View.VISIBLE
                placeholderPay.visibility = View.GONE
            }
            listHistory.visibility = View.GONE
            placeholderHistory.visibility = View.GONE
        } else {
            if (historyItems.isEmpty()) {
                listHistory.visibility = View.GONE
                placeholderHistory.visibility = View.VISIBLE
            } else {
                listHistory.visibility = View.VISIBLE
                placeholderHistory.visibility = View.GONE
            }
            listToPay.visibility = View.GONE
            placeholderPay.visibility = View.GONE
        }
    }
}
