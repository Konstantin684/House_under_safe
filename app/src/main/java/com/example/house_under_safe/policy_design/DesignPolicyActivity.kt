package com.example.house_under_safe.policy_design

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.model.PropertyType

class DesignPolicyActivity : AppCompatActivity() {

    private lateinit var viewModel: PolicyDesignViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_design_policy)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[PolicyDesignViewModel::class.java]

        // Назначаем тип недвижимости из Intent
        val typeName = intent.getStringExtra("property_type")
        val propertyType = when (typeName) {
            PropertyType.CityResidential::class.simpleName -> PropertyType.CityResidential
            PropertyType.CountryResidential::class.simpleName -> PropertyType.CountryResidential
            PropertyType.CountryNotResidential::class.simpleName -> PropertyType.CountryNotResidential
            PropertyType.Commercial::class.simpleName -> PropertyType.Commercial
            PropertyType.Industrial::class.simpleName -> PropertyType.Industrial
            else -> throw IllegalArgumentException("Unknown type")
        }
        viewModel.selectedPropertyType.value = propertyType

        // Кнопка "Назад"
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            showSaveDraftDialog()
        }
    }

    private fun showSaveDraftDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Сохранить черновик?")
            .setMessage("Сохранить введенные данные перед выходом?")
            .setPositiveButton("Да") { _, _ ->
                saveDraft()
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
            .setNegativeButton("Нет") { _, _ ->
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
            .setCancelable(true)
            .show()
    }

    private fun saveDraft() {
        val property = viewModel.propertyInfo.value
        val insurer = viewModel.insurerInfo.value
        val conditions = viewModel.insuranceConditions.value

        Log.d("Draft", "Сохраняем черновик: $property $insurer $conditions")

        // Здесь можно сохранить во временное хранилище, если нужно
    }

    fun updateProgress(step: Int, totalSteps: Int = 5) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progress = (step.toFloat() / totalSteps.toFloat() * 100).toInt()
        ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
}
