package com.example.house_under_safe.profile_details

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.house_under_safe.MainActivity
import com.example.house_under_safe.R
import com.example.house_under_safe.about_app.AboutAppActivity
import com.example.house_under_safe.databinding.ActivityProfileDetailsBinding
import com.example.house_under_safe.passport.PassportActivity
import com.example.house_under_safe.ui.profile.ProfileFragment
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ProfileDetailsActivity : AppCompatActivity() {

    private lateinit var prefs: Context
    private lateinit var editFields: List<EditText>

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.avatarImage.setImageURI(it)
            saveAvatarToInternalStorage(it)?.let { path ->
                getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                    .edit().putString("avatar_path", path).apply()
            }
        }
    }

    private var _binding: ActivityProfileDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = applicationContext

        // Собираем список редактируемых полей
        editFields = listOf(
            binding.editTextEmail,
            binding.editTextPhone
        )

        loadSavedData()
        loadSavedAvatar()
        setFieldsEnabled(false)

        binding.btnChangePhoto.setOnClickListener { pickImage.launch("image/*") }

        binding.buttonEdit.setOnClickListener {
            setFieldsEnabled(true)
            binding.buttonSave.isEnabled = true
            binding.buttonEdit.isEnabled = false
        }

        binding.buttonSave.setOnClickListener {
            saveData()
            setFieldsEnabled(false)
            binding.buttonSave.isEnabled = false
            binding.buttonEdit.isEnabled = true
        }

        binding.passportCard.setOnClickListener {
            startActivity(Intent(this, PassportActivity::class.java))
        }

        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun setFieldsEnabled(enabled: Boolean) {
        editFields.forEach { it.isEnabled = enabled }
    }

    private fun saveData() {
        getSharedPreferences("profile_prefs", Context.MODE_PRIVATE).edit()
            .putString("email", binding.editTextEmail.text.toString())
            .putString("phone", binding.editTextPhone.text.toString())
            .apply()
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        binding.editTextEmail.setText(prefs.getString("email", ""))
        binding.editTextPhone.setText(prefs.getString("phone", ""))
    }

    private fun loadSavedAvatar() {
        val path = getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
            .getString("avatar_path", null)
        path?.let {
            val file = File(it)
            if (file.exists()) {
                binding.avatarImage.setImageURI(Uri.fromFile(file))
            }
        }
    }

    private fun saveAvatarToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(filesDir, "avatar.jpg")
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
