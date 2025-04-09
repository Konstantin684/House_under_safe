package com.example.house_under_safe.design_theme

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.house_under_safe.R

class DesignThemeActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design_theme)

        prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)

        findViewById<ImageButton>(R.id.btn_back_design).setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val radioGroup = findViewById<RadioGroup>(R.id.theme_radio_group)

        when (prefs.getString("theme", THEME_SYSTEM)) {
            THEME_LIGHT -> radioGroup.check(R.id.radioButton_ligth_theme)
            THEME_DARK -> radioGroup.check(R.id.radioButton_dark_theme)
            else -> radioGroup.check(R.id.radioButton_system_theme)
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton_ligth_theme -> setThemeAndRestart(THEME_LIGHT, AppCompatDelegate.MODE_NIGHT_NO)
                R.id.radioButton_dark_theme -> setThemeAndRestart(THEME_DARK, AppCompatDelegate.MODE_NIGHT_YES)
                R.id.radioButton_system_theme -> setThemeAndRestart(THEME_SYSTEM, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    private fun setThemeAndRestart(theme: String, mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        prefs.edit().putString("theme", theme).apply()
        recreate()
    }

    companion object {
        private const val THEME_LIGHT = "light"
        private const val THEME_DARK = "dark"
        private const val THEME_SYSTEM = "system"
    }
}
