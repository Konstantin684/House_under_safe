package com.example.house_under_safe.policy_design.step_2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentSecondStepBinding
import com.example.house_under_safe.model.InsurerInfo
import com.example.house_under_safe.policy_design.DesignPolicyActivity
import com.example.house_under_safe.policy_design.PolicyDesignViewModel
import com.example.house_under_safe.policy_design.step_3.ThirdStepFragment
import java.util.Locale

class SecondStepFragment : Fragment(R.layout.fragment_second_step) {

    private lateinit var binding: FragmentSecondStepBinding
    private lateinit var viewModel: PolicyDesignViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSecondStepBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[PolicyDesignViewModel::class.java]

        setFormattedTextWatcher(binding.passportVydan,"upper")
        setFormattedTextWatcher(binding.dataVydachi, "date")
        setFormattedTextWatcher(binding.kodPodrazdelenia, "code")
        setFormattedTextWatcher(binding.seriaINomer, "seriesAndNumber")
        setFormattedTextWatcher(binding.familia,"upper")
        setFormattedTextWatcher(binding.imy,"upper")
        setFormattedTextWatcher(binding.otchestvo,"upper")
        setFormattedTextWatcher(binding.dataRojdenia, "date")
        setFormattedTextWatcher(binding.mestoRojdenia,"upper")
        setFormattedTextWatcher(binding.adresRegistracii,"upper")
        setFormattedTextWatcher(binding.adresProjivania,"upper")

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

    fun setFormattedTextWatcher(editText: EditText, type: String) {
        editText.addTextChangedListener(object : TextWatcher {
            var isFormatting = false

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Ничего не делаем
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Ничего не делаем
            }

            override fun afterTextChanged(editable: Editable?) {
                // Защита от бесконечной рекурсии
                if (isFormatting) return
                editable?.let {
                    var input = editable.toString()
                    // Обработаем только если нужно преобразовать в верхний регистр
                    if (type == "upper") {
                        val upperCaseText = input.toUpperCase(Locale.US) // Преобразуем в верхний регистр
                        if (input != upperCaseText) {
                            // Преобразуем в верхний регистр, но не сбиваем позицию курсора
                            isFormatting = true
                            editText.setText(upperCaseText)
                            editText.setSelection(upperCaseText.length) // Устанавливаем курсор в конец текста
                            isFormatting = false
                        }
                    } else {
                        when (type) {
                            "date" -> {
                                val maxLength = 10
                                if (input.length > maxLength) {
                                    input = input.substring(0, maxLength)
                                }
                                input = input.replace(".", "")  // Убираем все символы ":"
                                if (input.length > 2) {
                                    input = input.substring(0, 2) + "." + input.substring(2)
                                }
                                if (input.length > 5) {
                                    input = input.substring(0, 5) + "." + input.substring(5)
                                }
                                if (input.length > 10) {
                                    input = input.substring(0, 10)
                                }
                            }
                            "code" -> {
                                val maxLength = 7
                                if (input.length > maxLength) {
                                    input = input.substring(0, maxLength)
                                }
                                input = input.replace("-", "")  // Убираем все символы ":"
                                if (input.length > 3) {
                                    input = input.substring(0, 3) + "-" + input.substring(3)
                                }
                                if (input.length > 7) {
                                    input = input.substring(0, 7)
                                }
                            }
                            "seriesAndNumber" -> {
                                // Форматирование серии и номера XX XX XXXXXX
                                val maxLength = 12
                                if (input.length > maxLength) {
                                    input = input.substring(0, maxLength)
                                }
                                input = input.replace(" ", "")  // Убираем все символы ":"
                                if (input.length > 2) {
                                    input = input.substring(0, 2) + " " + input.substring(2)
                                }
                                if (input.length > 5) {
                                    input = input.substring(0, 5) + " " + input.substring(5)
                                }
                                if (input.length > 12) {
                                    input = input.substring(0, 12)
                                }
                            }
                        }
                        // Если текст изменился, обновляем содержимое EditText
                        if (editable.toString() != input) {
                            isFormatting = true
                            editText.setText(input)
                            editText.setSelection(input.length) // Устанавливаем курсор в конец текста
                            isFormatting = false
                        }
                    }
                }
            }
        })
    }

}