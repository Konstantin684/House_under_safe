package com.example.house_under_safe.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R

class NotificationsAdapter(
    private var allItems: List<NotificationItem>
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    private var filteredItems: List<NotificationItem> = allItems

    fun filterByType(type: NotificationType) {
        filteredItems = if (type == NotificationType.ALL) {
            allItems
        } else {
            allItems.filter { it.type == type }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = filteredItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = filteredItems.size

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeText: TextView = itemView.findViewById(R.id.transactionType)
        private val dateTimeText: TextView = itemView.findViewById(R.id.transactionDateTime)
        private val titleText: TextView = itemView.findViewById(R.id.textTitle)
        private val messageText: TextView = itemView.findViewById(R.id.textMessage)

        fun bind(item: NotificationItem) {
            typeText.text = when (item.type) {
                NotificationType.PAYMENT -> "Платежи"
                NotificationType.CLAIM -> "Возмещение ущерба"
                else -> ""
            }
            dateTimeText.text = item.date
            titleText.text = item.title
            messageText.text = item.message
        }
    }
}
