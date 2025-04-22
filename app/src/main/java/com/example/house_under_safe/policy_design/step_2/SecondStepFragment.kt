package com.example.house_under_safe.policy_design.step_2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentSecondStepBinding
import com.example.house_under_safe.model.InsurerInfo
import com.example.house_under_safe.policy_design.DesignPolicyActivity
import com.example.house_under_safe.policy_design.PolicyDesignViewModel
import com.example.house_under_safe.policy_design.step_3.ThirdStepFragment

class SecondStepFragment : Fragment(R.layout.fragment_second_step) {

    private lateinit var binding: FragmentSecondStepBinding
    private lateinit var viewModel: PolicyDesignViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSecondStepBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[PolicyDesignViewModel::class.java]

        restoreInputs()

        binding.imageButton3.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            (requireActivity() as? DesignPolicyActivity)?.updateProgress(0)
        }

        binding.imageButton4.setOnClickListener {
            val success = saveData()
            if (success) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, ThirdStepFragment())
                    .addToBackStack(null)
                    .commit()
                (requireActivity() as? DesignPolicyActivity)?.updateProgress(2)
            }
        }
    }

    private fun saveData(): Boolean {
        val vydan = binding.passportVydan.text.toString().trim()
        val dateVydachi = binding.dataVydachi.text.toString().trim()
        val kod = binding.kodPodrazdelenia.text.toString().trim()
        val seriaNomer = binding.seriaINomer.text.toString().trim().ifEmpty { null }
        val lastname = binding.familia.text.toString().trim()
        val name = binding.imy.text.toString().trim()
        val otchestvo = binding.otchestvo.text.toString().trim()
        val dateBirth = binding.dataRojdenia.text.toString().trim()
        val placeBirth = binding.mestoRojdenia.text.toString().trim()
        val adressReg = binding.adresRegistracii.text.toString().trim()
        val adressLive = binding.adresProjivania.text.toString().trim()

        if (vydan.isEmpty() || dateVydachi.isEmpty() || kod.isEmpty() ||
            lastname.isEmpty() || name.isEmpty() || otchestvo.isEmpty() ||
            dateBirth.isEmpty() || placeBirth.isEmpty() ||
            adressReg.isEmpty() || adressLive.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Заполните все обязательные поля", Toast.LENGTH_SHORT).show()
            return false
        }

        val info = InsurerInfo(
            passportVydan = vydan,
            dateVydachi = dateVydachi,
            kodPodrazdel = kod,
            passportNumber = seriaNomer,
            name = name,
            lastname = lastname,
            otchestvo = otchestvo,
            dateBirth = dateBirth,
            placeBirth = placeBirth,
            adressRegistry = adressReg,
            adressLive = adressLive
        )

        // Сохраняем во ViewModel
        viewModel.insurerInfo.value = info

        return true
    }

    private fun restoreInputs() {
        viewModel.insurerInfo.value?.let {
            binding.passportVydan.setText(it.passportVydan)
            binding.dataVydachi.setText(it.dateVydachi)
            binding.kodPodrazdelenia.setText(it.kodPodrazdel)
            binding.seriaINomer.setText(it.passportNumber ?: "")
            binding.familia.setText(it.lastname)
            binding.imy.setText(it.name)
            binding.otchestvo.setText(it.otchestvo)
            binding.dataRojdenia.setText(it.dateBirth)
            binding.mestoRojdenia.setText(it.placeBirth)
            binding.adresRegistracii.setText(it.adressRegistry)
            binding.adresProjivania.setText(it.adressLive)
        }
    }
}
