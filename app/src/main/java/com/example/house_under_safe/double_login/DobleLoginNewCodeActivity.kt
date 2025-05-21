package com.example.house_under_safe.double_login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.house_under_safe.MainActivity
import com.example.house_under_safe.R

class DobleLoginNewCodeActivity : AppCompatActivity() {

    private lateinit var codeDots: List<View>
    private lateinit var dotsContainer: LinearLayout
    private val enteredCode = StringBuilder()
    private var codeStage = 0 // 0 - придумайте код, 1 - повторите код, 2 - проверка
    private var tempCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doble_login_new_code) // Укажи актуальный layout!

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Надпись сверху
        val enterCodeText = findViewById<TextView>(R.id.enterCodeText)

        // Точки для отображения введённых символов
        codeDots = listOf(
            findViewById(R.id.dot1),
            findViewById(R.id.dot2),
            findViewById(R.id.dot3),
            findViewById(R.id.dot4)
        )
        dotsContainer = findViewById(R.id.codeDots)

        // Обработка цифровых кнопок
        val numberIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                val digit = (it as Button).text.toString()
                handleDigitInput(digit)
            }
        }

        // Сценарий: установка нового кода, потом подтверждение
        // Если нужно просто проверять код — сделай codeStage = 2
        val prefs = getSharedPreferences("pin_prefs", Context.MODE_PRIVATE)
        val savedPin = prefs.getString("user_pin", null)
        codeStage = if (savedPin.isNullOrEmpty()) 0 else 2
        enterCodeText.text = when (codeStage) {
            0 -> "Придумайте код"
            1 -> "Повторите код"
            else -> "Введите код"
        }
        updateDots()
    }

    private fun handleDigitInput(digit: String) {
        if (enteredCode.length < 4) {
            enteredCode.append(digit)
            updateDots()
        }
        if (enteredCode.length == 4) {
            when (codeStage) {
                0 -> {
                    // Сохраняем первый ввод, просим повторить
                    tempCode = enteredCode.toString()
                    enteredCode.clear()
                    updateDots()
                    findViewById<TextView>(R.id.enterCodeText).text = "Повторите код"
                    codeStage = 1
                }
                1 -> {
                    // Сравниваем с предыдущим вводом
                    if (enteredCode.toString() == tempCode) {
                        // Устанавливаем PIN
                        val prefs = getSharedPreferences("pin_prefs", Context.MODE_PRIVATE)
                        prefs.edit().putString("user_pin", enteredCode.toString()).apply()
                        // Можно показать успех и перейти дальше
                        finishSuccess()
                    } else {
                        playShakeAnimation()
                        enteredCode.clear()
                        updateDots()
                        findViewById<TextView>(R.id.enterCodeText).text = "Коды не совпали, попробуйте ещё раз"
                        codeStage = 0
                    }
                }
                2 -> {
                    // Проверяем сохранённый PIN
                    val prefs = getSharedPreferences("pin_prefs", Context.MODE_PRIVATE)
                    val savedPin = prefs.getString("user_pin", null)
                    if (enteredCode.toString() == savedPin) {
                        finishSuccess()
                    } else {
                        playShakeAnimation()
                        enteredCode.clear()
                        updateDots()
                    }
                }
            }
        }
    }

    private fun updateDots() {
        codeDots.forEachIndexed { index, view ->
            view.setBackgroundResource(
                if (index < enteredCode.length)
                    R.drawable.dot_circle_field // активная точка
                else
                    R.drawable.dot_circle_empty // неактивная
            )
        }
    }

    private fun playShakeAnimation() {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        dotsContainer.startAnimation(shake)
    }

    private fun finishSuccess() {
        // Можно запускать нужную Activity, закрывать экран или что-то ещё
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
