package com.example.house_under_safe.ui.payments.to_pay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R
import com.example.house_under_safe.ui.payments.*

class PaymentsToPayFragment : Fragment(), OnPaymentClickListener {

    private val viewModel: PaymentsViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholder: View
    private lateinit var adapter: PaymentToPayAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_payments_to_pay, container, false)
        recyclerView = view.findViewById(R.id.recyclerToPay)
        placeholder = view.findViewById(R.id.placeholder_pay)

        adapter = PaymentToPayAdapter(mutableListOf(), viewModel, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        observePayments()

        return view
    }

    override fun onPayClicked(paymentItem: PaymentItem) {
        // Например, просто обновляем статус на "Оплачен" с текущей датой
        val updated = paymentItem.copy(
            status = PaymentStatus.PAID,
            datetime = getCurrentDateTimeString()
        )
        viewModel.updatePayment(updated)

        Toast.makeText(requireContext(), "Оплата выполнена по счёту № ${paymentItem.number}", Toast.LENGTH_SHORT).show()
    }

    private fun observePayments() {
        viewModel.payments.observe(viewLifecycleOwner, Observer { payments ->
            val toPayItems = payments.filter { it.status == PaymentStatus.AWAITING }
            adapter.updateItems(toPayItems.toMutableList())

            placeholder.visibility = if (toPayItems.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.visibility = if (toPayItems.isEmpty()) View.GONE else View.VISIBLE
        })
    }

    private fun getCurrentDateTimeString(): String {
        val formatter = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }
}


