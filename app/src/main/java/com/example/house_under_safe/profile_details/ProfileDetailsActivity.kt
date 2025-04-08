package com.example.house_under_safe.profile_details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.house_under_safe.R
import java.io.File
import java.io.FileOutputStream

class ProfileDetailsActivity : AppCompatActivity() {

    private lateinit var avatarImage: ImageView
    private lateinit var btnChangePhoto: TextView

    private val pickImageRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)

        avatarImage = findViewById(R.id.avatarImage)
        btnChangePhoto = findViewById(R.id.btn_change_photo)

        loadSavedAvatar()

        btnChangePhoto.setOnClickListener {
            pickImageFromGallery()
        }

        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener {
            finish()
        }
    }

    private fun loadSavedAvatar() {
        val prefs = getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        val path = prefs.getString("avatar_path", null)
        path?.let {
            val file = File(it)
            if (file.exists()) {
                avatarImage.setImageURI(Uri.fromFile(file))
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, pickImageRequestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequestCode && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                avatarImage.setImageURI(it)
                val savedPath = saveAvatarToInternalStorage(it)
                savedPath?.let { path ->
                    getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                        .edit()
                        .putString("avatar_path", path)
                        .apply()
                }
            }
        }
    }

    private fun saveAvatarToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "avatar.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
