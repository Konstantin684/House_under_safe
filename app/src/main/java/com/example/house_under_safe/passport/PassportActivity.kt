package com.example.house_under_safe.passport

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.ActivityPassportBinding
import java.io.File
import java.io.FileOutputStream

class PassportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPassportBinding
    private lateinit var prefs: Context

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.passportImage.setImageURI(it)
            binding.textView14.visibility = View.GONE
            savePassportToInternalStorage(it)?.let { path ->
                getSharedPreferences("passport_prefs", Context.MODE_PRIVATE)
                    .edit().putString("passport_path", path).apply()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPassportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = applicationContext

        val editFields = listOf(
            binding.passportVydan,
            binding.dataVydachi,
            binding.kodPodrazdelenia,
            binding.seriaINomer,
            binding.familia,
            binding.imy,
            binding.otchestvo,
            binding.dataRojdenia,
            binding.mestoRojdenia
        )

        loadSavedData()
        loadSavedAvatar()
        setFieldsEnabled(editFields, false)

        binding.passportCard.setOnClickListener { pickImage.launch("image/*") }

        binding.buttonEdit.setOnClickListener {
            setFieldsEnabled(editFields, true)
            binding.buttonSave.isEnabled = true
            binding.buttonEdit.isEnabled = false
        }

        binding.buttonSave.setOnClickListener {
            saveData()
            setFieldsEnabled(editFields, false)
            binding.buttonSave.isEnabled = false
            binding.buttonEdit.isEnabled = true
        }

        findViewById<ImageView>(R.id.btn_back_passport)?.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun setFieldsEnabled(fields: List<android.widget.EditText>, enabled: Boolean) {
        fields.forEach { it.isEnabled = enabled }
    }

    private fun saveData() {
        getSharedPreferences("passport_prefs", Context.MODE_PRIVATE).edit()
            .putString("passport_vydan", binding.passportVydan.text.toString())
            .putString("data_vydachi", binding.dataVydachi.text.toString())
            .putString("kod_pod", binding.kodPodrazdelenia.text.toString())
            .putString("SaN", binding.seriaINomer.text.toString())
            .putString("lastName", binding.familia.text.toString())
            .putString("firstName", binding.imy.text.toString())
            .putString("patronymic", binding.otchestvo.text.toString())
            .putString("dob", binding.dataRojdenia.text.toString())
            .putString("pob", binding.mestoRojdenia.text.toString())
            .apply()
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("passport_prefs", Context.MODE_PRIVATE)
        binding.passportVydan.setText(prefs.getString("passport_vydan", ""))
        binding.dataVydachi.setText(prefs.getString("data_vydachi", ""))
        binding.kodPodrazdelenia.setText(prefs.getString("kod_pod", ""))
        binding.seriaINomer.setText(prefs.getString("SaN", ""))
        binding.familia.setText(prefs.getString("lastName", ""))
        binding.imy.setText(prefs.getString("firstName", ""))
        binding.otchestvo.setText(prefs.getString("patronymic", ""))
        binding.dataRojdenia.setText(prefs.getString("dob", ""))
        binding.mestoRojdenia.setText(prefs.getString("pob", ""))
    }

    private fun loadSavedAvatar() {
        val path = getSharedPreferences("passport_prefs", Context.MODE_PRIVATE)
            .getString("passport_path", null)
        path?.let {
            val file = File(it)
            if (file.exists()) {
                binding.passportImage.setImageURI(Uri.fromFile(file))
                binding.textView14.visibility = View.GONE
            }
        }
    }

    private fun savePassportToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(filesDir, "passport.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
