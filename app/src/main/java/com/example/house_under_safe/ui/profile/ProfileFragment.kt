package com.example.house_under_safe.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.databinding.FragmentProfileBinding
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

        // Загружаем путь к изображению из SharedPreferences
        val prefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        val avatarPath = prefs.getString("avatar_path", null)

        if (!avatarPath.isNullOrEmpty()) {
            val file = File(avatarPath)
            if (file.exists()) {
                binding.avatarImage.setImageURI(Uri.fromFile(file))
            }
        }

        // Загружаем и отображаем ФИО
        val lastName = prefs.getString("lastName", "") ?: ""
        val firstName = prefs.getString("firstName", "") ?: ""
        val patronymic = prefs.getString("patronymic", "") ?: ""
        val fullName = "$lastName $firstName $patronymic".trim().uppercase()

        binding.profileFio.text = fullName // убедись, что такое поле есть в layout

        // Переход на ProfileDetailsActivity по нажатию на карточку профиля
        binding.profileCard.setOnClickListener {
            val intent = Intent(requireContext(), ProfileDetailsActivity::class.java)
            startActivity(intent)
        }

        // Переход на экран темы оформления
        binding.designThemeCard.setOnClickListener {
            val intent = Intent(requireContext(), com.example.house_under_safe.design_theme.DesignThemeActivity::class.java)
            startActivity(intent)
        }

        profileViewModel.text.observe(viewLifecycleOwner) {
            // При необходимости отобразить текст
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
