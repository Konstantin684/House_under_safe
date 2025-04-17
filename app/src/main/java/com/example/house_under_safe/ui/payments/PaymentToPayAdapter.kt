package com.example.house_under_safe.ui.payments

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R

class PaymentToPayAdapter(
    private val items: MutableList<PaymentItem>,
    private val viewModel: PaymentsViewModel
) : RecyclerView.Adapter<PaymentToPayAdapter.PaymentToPayViewHolder>() {

    // Храним время начала таймера для каждой позиции
    private val startTimes = mutableMapOf<String, Long>()
    private val timerDuration = 1 * 60 * 1000L // 15 минут в миллисекундах

    inner class PaymentToPayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val paymentNumber: TextView = view.findViewById(R.id.payment_number)
        val paymentPolicy: TextView = view.findViewById(R.id.payment_policy)
        val paymentStatus: TextView = view.findViewById(R.id.payment_status)
        val paymentAmount: TextView = view.findViewById(R.id.payment_amount)
        val btnPay: Button = view.findViewById(R.id.btn_pay)
        val timerText: TextView = view.findViewById(R.id.timer_text)
        var countDownTimer: CountDownTimer? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentToPayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_to_pay, parent, false)
        return PaymentToPayViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentToPayViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        holder.countDownTimer?.cancel()

        holder.paymentNumber.text = "СЧЕТ № ${item.number}"
        holder.paymentPolicy.text = item.policy
        holder.paymentAmount.text = item.amount

        val (statusText, statusColorRes) = when (item.status) {
            PaymentStatus.AWAITING -> "Ожидает оплаты" to R.color.orange
            PaymentStatus.CANCELED -> "Отменен" to R.color.red
            else -> "Неизвестно" to R.color.text_color_description
        }
        holder.paymentStatus.text = statusText
        holder.paymentStatus.setTextColor(ContextCompat.getColor(context, statusColorRes))

        if (item.status == PaymentStatus.AWAITING) {
            holder.btnPay.visibility = View.VISIBLE
            holder.timerText.visibility = View.VISIBLE
            holder.btnPay.isEnabled = true

            // Запускаем таймер, если ещё не запущен
            val startedAt = startTimes.getOrPut(item.number) { System.currentTimeMillis() }
            val remaining = (startedAt + timerDuration) - System.currentTimeMillis()

            if (remaining > 0) {
                holder.countDownTimer = object : CountDownTimer(remaining, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val min = millisUntilFinished / 60000
                        val sec = (millisUntilFinished % 60000) / 1000
                        holder.timerText.text = String.format("%02d:%02d", min, sec)
                    }

                    override fun onFinish() {
                        val canceledItem = item.copy(
                            status = PaymentStatus.CANCELED,
                            datetime = getCurrentDateTimeString()
                        )
                        items[position] = canceledItem
                        viewModel.updatePayment(canceledItem)
                        notifyItemChanged(position)
                    }
                }.start()
            } else {
                val canceledItem = item.copy(
                    status = PaymentStatus.CANCELED,
                    datetime = getCurrentDateTimeString()
                )
                items[position] = canceledItem
                viewModel.updatePayment(canceledItem)
                notifyItemChanged(position)
            }

            holder.btnPay.setOnClickListener {
                Toast.makeText(context, "Оплата по счёту № ${item.number}", Toast.LENGTH_SHORT).show()
                val paidItem = item.copy(
                    status = PaymentStatus.PAID,
                    datetime = getCurrentDateTimeString()
                )
                viewModel.updatePayment(paidItem)
            }

        } else {
            holder.btnPay.visibility = View.GONE
            holder.timerText.visibility = View.GONE
        }
    }

    override fun onViewRecycled(holder: PaymentToPayViewHolder) {
        holder.countDownTimer?.cancel()
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: MutableList<PaymentItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun getCurrentDateTimeString(): String {
        val formatter = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }
}
