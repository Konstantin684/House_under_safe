package com.example.house_under_safe.notifications

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R
import com.google.android.material.chip.ChipGroup

class NotificationsActivity : AppCompatActivity() {

    private lateinit var adapter: NotificationsAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var placeholderAll: View
    private lateinit var placeholderPayments: View
    private lateinit var placeholderClaims: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notifications)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        recyclerView = findViewById(R.id.recyclerViewTransactions)
        placeholderAll = findViewById(R.id.placeholderAll)
        placeholderPayments = findViewById(R.id.placeholderPayments)
        placeholderClaims = findViewById(R.id.placeholderClaims)

        adapter = NotificationsAdapter(getDummyNotifications())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Показ уведомлений по умолчанию (все)
        filterAndShow(NotificationType.ALL)

        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupFilters)
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val type = when (checkedId) {
                R.id.chipPayments -> NotificationType.PAYMENT
                R.id.chipClaims -> NotificationType.CLAIM
                else -> NotificationType.ALL
            }
            filterAndShow(type)
        }
    }

    private fun filterAndShow(type: NotificationType) {
        adapter.filterByType(type)

        // Скрыть все плейсхолдеры сначала
        placeholderAll.visibility = View.GONE
        placeholderPayments.visibility = View.GONE
        placeholderClaims.visibility = View.GONE

        // Показать нужный плейсхолдер, если список пуст
        if (adapter.itemCount == 0) {
            recyclerView.visibility = View.GONE
            when (type) {
                NotificationType.ALL -> placeholderAll.visibility = View.VISIBLE
                NotificationType.PAYMENT -> placeholderPayments.visibility = View.VISIBLE
                NotificationType.CLAIM -> placeholderClaims.visibility = View.VISIBLE
            }
        } else {
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun getDummyNotifications(): List<NotificationItem> {
        return listOf(
              /* NotificationItem(
                NotificationType.CLAIM,
                "Заявление одобрено",
                "Заявление на возмещение ущерба по полису №5165160651 одобрено",
                "22.11.2024 в 17:55"
            ),
            NotificationItem(
                NotificationType.PAYMENT,
                "Оплата успешно произведена",
                "Оплата полиса страхования №1234567890",
                "22.11.2024 в 19:03"
            ),
            NotificationItem(
                NotificationType.CLAIM,
                "Заявление сформировано",
                "Заявление на возмещение ущерба по полису №5165160651 успешно сформировано",
                "21.11.2024 в 17:54"
            ),
             NotificationItem(
                NotificationType.PAYMENT,
                "Оплата успешно произведена",
                "Оплата полиса страхования №5165160651",
                "13.03.2024 в 19:03"
            ) */
        )
    }
}
