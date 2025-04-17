package com.example.house_under_safe.ui.payments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R
import com.example.house_under_safe.ui.payments.PaymentHistoryAdapter
import com.example.house_under_safe.ui.payments.mockPaymentItems

class PaymentsHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_paymets_history, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerHistory)
        val placeholder = view.findViewById<View>(R.id.placeholder_history)

        val historyItems = mockPaymentItems.filter { it.datetime != null }

        if (historyItems.isEmpty()) {
            recyclerView.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        } else {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = PaymentHistoryAdapter(historyItems)
            recyclerView.visibility = View.VISIBLE
            placeholder.visibility = View.GONE
        }

        return view
    }
}
