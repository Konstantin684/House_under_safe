package com.example.house_under_safe.double_login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.house_under_safe.MainActivity
import com.example.house_under_safe.R

class DoubleLoginActivity : AppCompatActivity() {

    private lateinit var codeDots: List<View>
    private lateinit var dotsContainer: LinearLayout
    private val enteredCode = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_double_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Контейнер, который будет трястись
        dotsContainer = findViewById(R.id.codeDots)

        // Точки ввода кода
        codeDots = listOf(
            findViewById(R.id.dot1),
            findViewById(R.id.dot2),
            findViewById(R.id.dot3),
            findViewById(R.id.dot4)
        )

        // Обработка кнопок 0–9
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

        findViewById<Button>(R.id.btnExit)?.setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.btnFingerprint)?.setOnClickListener {
            // TODO: Биометрия
        }
    }

    private fun handleDigitInput(digit: String) {
        if (enteredCode.length < 4) {
            enteredCode.append(digit)
            updateDots()
        }

        if (enteredCode.length == 4) {
            checkCodeAndProceed()
        }
    }

    private fun updateDots() {
        codeDots.forEachIndexed { index, view ->
            view.setBackgroundResource(
                if (index < enteredCode.length)
                    R.drawable.dot_circle_field
                else
                    R.drawable.dot_circle_empty
            )
        }
    }

    private fun checkCodeAndProceed() {
        val correctCode = "1234"

        if (enteredCode.toString() == correctCode) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            enteredCode.clear()
            updateDots()
            playShakeAnimation()
        }
    }

    private fun playShakeAnimation() {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        dotsContainer.startAnimation(shake)
    }
}
