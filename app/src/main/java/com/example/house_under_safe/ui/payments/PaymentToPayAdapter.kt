package com.example.house_under_safe.ui.payments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R

class PaymentToPayAdapter(
    private val items: List<PaymentItem>
) : RecyclerView.Adapter<PaymentToPayAdapter.PaymentToPayViewHolder>() {

    inner class PaymentToPayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val paymentNumber: TextView = view.findViewById(R.id.payment_number)
        val paymentPolicy: TextView = view.findViewById(R.id.payment_policy)
        val paymentStatus: TextView = view.findViewById(R.id.payment_status)
        val paymentAmount: TextView = view.findViewById(R.id.payment_amount)
        val btnPay: Button = view.findViewById(R.id.btn_pay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentToPayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_to_pay, parent, false)
        return PaymentToPayViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentToPayViewHolder, position: Int) {
        val item = items[position]
        holder.paymentNumber.text = "СЧЕТ № ${item.number}"
        holder.paymentPolicy.text = item.policy
        holder.paymentStatus.text = item.status
        holder.paymentAmount.text = item.amount

        holder.btnPay.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Оплата по счёту № ${item.number}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = items.size
}
