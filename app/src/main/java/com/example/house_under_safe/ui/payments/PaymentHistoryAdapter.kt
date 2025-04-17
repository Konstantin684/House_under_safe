package com.example.house_under_safe.ui.payments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R

class PaymentHistoryAdapter(
    private val items: MutableList<PaymentItem>
) : RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder>() {

    inner class PaymentHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val paymentNumber: TextView = view.findViewById(R.id.payment_number)
        val paymentPolicy: TextView = view.findViewById(R.id.payment_policy)
        val paymentStatus: TextView = view.findViewById(R.id.payment_status)
        val paymentAmount: TextView = view.findViewById(R.id.payment_amount)
        val paymentDatetime: TextView = view.findViewById(R.id.payment_datetime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_history, parent, false)
        return PaymentHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        val item = items[position]
        holder.paymentNumber.text = item.number
        holder.paymentPolicy.text = item.policy
        holder.paymentAmount.text = item.amount
        holder.paymentDatetime.text = item.datetime ?: ""

        val context = holder.itemView.context
        when (item.status) {
            PaymentStatus.PAID -> {
                holder.paymentStatus.text = "Оплачен"
                holder.paymentStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
            PaymentStatus.CANCELED -> {
                holder.paymentStatus.text = "Отменен"
                holder.paymentStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            else -> {
                holder.paymentStatus.text = "Неизвестно"
                holder.paymentStatus.setTextColor(ContextCompat.getColor(context, R.color.text_color_all))
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<PaymentItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
