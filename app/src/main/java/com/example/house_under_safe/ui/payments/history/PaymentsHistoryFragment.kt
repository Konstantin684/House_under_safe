package com.example.house_under_safe.ui.payments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R
import com.example.house_under_safe.ui.payments.PaymentHistoryAdapter
import com.example.house_under_safe.ui.payments.PaymentItem
import com.example.house_under_safe.ui.payments.PaymentStatus
import com.example.house_under_safe.ui.payments.PaymentsViewModel

class PaymentsHistoryFragment : Fragment() {

    private val viewModel: PaymentsViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholder: View
    private lateinit var adapter: PaymentHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_paymets_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerHistory)
        placeholder = view.findViewById(R.id.placeholder_history)

        adapter = PaymentHistoryAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        observePayments()

        return view
    }

    private fun observePayments() {
        viewModel.payments.observe(viewLifecycleOwner, Observer { allPayments ->
            val historyItems = allPayments.filter { it.status != PaymentStatus.AWAITING }

            if (historyItems.isEmpty()) {
                recyclerView.visibility = View.GONE
                placeholder.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                placeholder.visibility = View.GONE
                adapter.updateItems(historyItems)
            }
        })
    }
}
