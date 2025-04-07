package com.example.house_under_safe.ui.payments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.house_under_safe.R

class PaymentToPayAdapter(
    private val context: Context,
    private val items: List<PaymentItem>
) : BaseAdapter() {

    override fun getCount() = items.size
    override fun getItem(position: Int) = items[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_payment_to_pay, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position)

        holder.paymentNumber.text = "СЧЕТ № ${item.number}"
        holder.paymentPolicy.text = item.policy
        holder.paymentStatus.text = item.status
        holder.paymentAmount.text = item.amount

        holder.btnPay.setOnClickListener {
            Toast.makeText(context, "Оплата по счёту № ${item.number}", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private class ViewHolder(view: View) {
        val paymentNumber: TextView = view.findViewById(R.id.payment_number)
        val paymentPolicy: TextView = view.findViewById(R.id.payment_policy)
        val paymentStatus: TextView = view.findViewById(R.id.payment_status)
        val paymentAmount: TextView = view.findViewById(R.id.payment_amount)
        val btnPay: Button = view.findViewById(R.id.btn_pay)
    }
}

