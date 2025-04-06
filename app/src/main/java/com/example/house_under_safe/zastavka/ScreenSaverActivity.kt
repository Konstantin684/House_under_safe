package com.example.house_under_safe.zastavka

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.house_under_safe.R
import com.example.house_under_safe.double_login.DoubleLoginActivity

class ScreenSaverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_screen_saver)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Запускаем переход с задержкой 3 секунды
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, DoubleLoginActivity::class.java))
            finish()
        }, 3000) // 3000 мс = 3 секунды
    }
}