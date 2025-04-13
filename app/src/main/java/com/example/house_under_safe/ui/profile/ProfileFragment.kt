package com.example.house_under_safe.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.about_app.AboutAppActivity
import com.example.house_under_safe.databinding.FragmentProfileBinding
import com.example.house_under_safe.design_theme.DesignThemeActivity
import com.example.house_under_safe.notifications.NotificationsActivity
import com.example.house_under_safe.profile_details.ProfileDetailsActivity
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupCardNavigation()
        loadProfileData() // первая загрузка данных

        profileViewModel.text.observe(viewLifecycleOwner) {
            // при необходимости
        }

        return root
    }

    // Загружаем данные при каждом возврате на экран
    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    private fun loadProfileData() {
        val passportPrefs = requireContext().getSharedPreferences("passport_prefs", Context.MODE_PRIVATE)
        val profilePrefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

        // Загружаем и устанавливаем ФИО
        val lastName = passportPrefs.getString("lastName", "") ?: ""
        val firstName = passportPrefs.getString("firstName", "") ?: ""
        val patronymic = passportPrefs.getString("patronymic", "") ?: ""
        val fullName = "$lastName $firstName $patronymic".trim().uppercase()
        binding.profileFio.text = fullName

        // Загружаем и устанавливаем аватар
        val avatarPath = profilePrefs.getString("avatar_path", null)
        if (!avatarPath.isNullOrEmpty()) {
            val file = File(avatarPath)
            if (file.exists()) {
                binding.avatarImage.setImageDrawable(null)
                binding.avatarImage.setImageURI(Uri.fromFile(file))
                binding.avatarImage.invalidate()

            }
        } else {
            binding.avatarImage.setImageResource(R.drawable.profile_incognito)
        }
        // Проверка подтверждения аккаунта по наличию изображения паспорта
        updateVerificationStatus(passportPrefs)
    }

    private fun setupCardNavigation() {
        // Переход на редактирование профиля
        binding.profileCard.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileDetailsActivity::class.java))
        }

        // Переход на уведомления
        binding.notificationCard.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationsActivity::class.java))
        }

        // Переход на смену темы
        binding.designThemeCard.setOnClickListener {
            startActivity(Intent(requireContext(), DesignThemeActivity::class.java))
        }

        // Переход на информацию о приложении
        binding.aboutAppCard.setOnClickListener {
            startActivity(Intent(requireContext(), AboutAppActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateVerificationStatus(passportPrefs: SharedPreferences) {
        val passportPath = passportPrefs.getString("passport_path", null)
        val file = passportPath?.let { File(it) }

        if (file != null && file.exists()) {
            binding.imageView4.setImageResource(R.drawable.checked_profile)
            binding.textView7.text = getString(R.string.accaunt_confirmed)
        } else {
            binding.imageView4.setImageResource(R.drawable.unchecked_profile)
            binding.textView7.text = getString(R.string.accaunt_not_confirm)
        }
    }
}
