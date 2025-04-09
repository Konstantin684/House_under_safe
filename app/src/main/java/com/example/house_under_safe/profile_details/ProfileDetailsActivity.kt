package com.example.house_under_safe.profile_details

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.house_under_safe.R
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ProfileDetailsActivity : AppCompatActivity() {

    private lateinit var avatarImage: ImageView
    private lateinit var btnChangePhoto: TextView
    private lateinit var buttonSave: Button
    private lateinit var buttonEdit: Button

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextDateOfBirth: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextPatronymic: EditText


    private lateinit var editFields: List<EditText>
    private lateinit var prefs: Context

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            avatarImage.setImageURI(it)
            saveAvatarToInternalStorage(it)?.let { path ->
                getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                    .edit().putString("avatar_path", path).apply()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)

        prefs = applicationContext
        avatarImage = findViewById(R.id.avatarImage)
        btnChangePhoto = findViewById(R.id.btn_change_photo)
        buttonSave = findViewById(R.id.button_save)
        buttonEdit = findViewById(R.id.button_edit)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextPatronymic = findViewById(R.id.editTextPatronymic)
        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth)

        editFields = listOf(editTextEmail, editTextPhone, editTextLastName,editTextFirstName,editTextPatronymic, editTextDateOfBirth)

        loadSavedData()
        loadSavedAvatar()
        setFieldsEnabled(false)

        btnChangePhoto.setOnClickListener { pickImage.launch("image/*") }

        buttonEdit.setOnClickListener {
            setFieldsEnabled(true)
            buttonSave.isEnabled = true
            buttonEdit.isEnabled = false
        }

        buttonSave.setOnClickListener {
            saveData()
            setFieldsEnabled(false)
            buttonSave.isEnabled = false
            buttonEdit.isEnabled = true
        }

        editTextDateOfBirth.setOnClickListener {
            if (editTextDateOfBirth.isEnabled) showDatePicker()
        }

        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener {
            finish()
        }
    }

    private fun setFieldsEnabled(enabled: Boolean) {
        editFields.forEach { it.isEnabled = enabled }
    }

    private fun saveData() {
        getSharedPreferences("profile_prefs", Context.MODE_PRIVATE).edit()
            .putString("email", editTextEmail.text.toString())
            .putString("phone", editTextPhone.text.toString())
            .putString("lastName", editTextLastName.text.toString())
            .putString("firstName", editTextFirstName.text.toString())
            .putString("patronymic", editTextPatronymic.text.toString())
            .putString("dob", editTextDateOfBirth.text.toString())
            .apply()
    }




    private fun loadSavedData() {
        val prefs = getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        editTextEmail.setText(prefs.getString("email", ""))
        editTextPhone.setText(prefs.getString("phone", ""))
        editTextLastName.setText(prefs.getString("lastName", ""))
        editTextFirstName.setText(prefs.getString("firstName", ""))
        editTextPatronymic.setText(prefs.getString("patronymic", ""))
        editTextDateOfBirth.setText(prefs.getString("dob", ""))
    }

    private fun loadSavedAvatar() {
        val path = getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
            .getString("avatar_path", null)
        path?.let {
            val file = File(it)
            if (file.exists()) {
                avatarImage.setImageURI(Uri.fromFile(file))
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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = "%02d.%02d.%d".format(day, month + 1, year)
                editTextDateOfBirth.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}
