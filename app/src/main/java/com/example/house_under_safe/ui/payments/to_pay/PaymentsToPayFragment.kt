package com.example.house_under_safe.ui.payments.to_pay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R
import com.example.house_under_safe.ui.payments.PaymentToPayAdapter
import com.example.house_under_safe.ui.payments.mockPaymentItems

class PaymentsToPayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_payments_to_pay, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerToPay)
        val placeholder = view.findViewById<View>(R.id.placeholder_pay)

        val toPayItems = mockPaymentItems.filter { it.datetime == null }

        if (toPayItems.isEmpty()) {
            recyclerView.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        } else {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = PaymentToPayAdapter(toPayItems)
            recyclerView.visibility = View.VISIBLE
            placeholder.visibility = View.GONE
        }

        return view
    }
}
