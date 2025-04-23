package com.example.house_under_safe.policy_design.step_1

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.PolicyStorageHelper
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentFirstStepBinding
import com.example.house_under_safe.model.*
import com.example.house_under_safe.policy_design.DesignPolicyActivity
import com.example.house_under_safe.policy_design.PolicyDesignViewModel
import com.example.house_under_safe.policy_design.step_2.SecondStepFragment
import java.util.Locale

class FirstStepFragment : Fragment(R.layout.fragment_first_step) {

    private lateinit var binding: FragmentFirstStepBinding
    private lateinit var viewModel: PolicyDesignViewModel
    private lateinit var subtypes: Array<out PropertySubtype>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFirstStepBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[PolicyDesignViewModel::class.java]
        restoreSavedInputs()

        //"upper"
        setFormattedTextWatcher(binding.region, "upper")
        setFormattedTextWatcher(binding.adresNedvig,"upper")
        setNumericFormattedTextWatcher(binding.obchaiaPlochad, 24)
        setNumericFormattedTextWatcher(binding.kolEtaj, 24)
        setNumericFormattedTextWatcher(binding.numberEtaj, 24)
        setFormattedTextWatcher(binding.kadastroviyNomer, "cadaster_number")

        binding.imageButton4.setOnClickListener {
            val success = saveDataToViewModel()
            if (success) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, SecondStepFragment())
                    .addToBackStack(null)
                    .commit()
                (requireActivity() as? DesignPolicyActivity)?.updateProgress(1)
            }
        }

        val propertyType = viewModel.selectedPropertyType.value
        if (propertyType != null) {
            subtypes = when (propertyType) {
                is PropertyType.CityResidential -> PropertyType.CityResidential.Subtype.values() as Array<out PropertySubtype>
                is PropertyType.CountryResidential -> PropertyType.CountryResidential.Subtype.values() as Array<out PropertySubtype>
                is PropertyType.CountryNotResidential -> PropertyType.CountryNotResidential.Subtype.values() as Array<out PropertySubtype>
                is PropertyType.Commercial -> PropertyType.Commercial.Subtype.values() as Array<out PropertySubtype>
                is PropertyType.Industrial -> PropertyType.Industrial.Subtype.values() as Array<out PropertySubtype>
                is PropertyType.ProdleniePolisa -> PropertyType.ProdleniePolisa.Subtype.values() as Array<out PropertySubtype>
            }

            val subtypeLabels = subtypes.map { it.label }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subtypeLabels)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPropertyType.adapter = adapter

            binding.spinnerPropertyType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    viewModel.selectedSubtype.value = subtypes[position]
                    viewModel.selectedSubtypeIndex.value = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    viewModel.selectedSubtype.value = null
                    viewModel.selectedSubtypeIndex.value = null
                }
            }

            viewModel.selectedSubtypeIndex.value?.let {
                binding.spinnerPropertyType.setSelection(it)
            }
        }
    }

    private fun saveDataToViewModel(): Boolean {
        val propertyType = viewModel.selectedPropertyType.value ?: return false

        val region = binding.region.text.toString().trim()
        val address = binding.adresNedvig.text.toString().trim()
        var areaText = binding.obchaiaPlochad.text.toString().trim()

        areaText = areaText.replace(" ", "")

        var isValid = true
        if (region.isEmpty()) { setErrorStyle(binding.region); isValid = false } else resetStyle(binding.region)
        if (address.isEmpty()) { setErrorStyle(binding.adresNedvig); isValid = false } else resetStyle(binding.adresNedvig)
        if (areaText.isEmpty()) { setErrorStyle(binding.obchaiaPlochad); isValid = false } else resetStyle(binding.obchaiaPlochad)

        val totalArea = areaText.toDoubleOrNull()
        if (totalArea == null) {
            setErrorStyle(binding.obchaiaPlochad)
            Toast.makeText(requireContext(), "Площадь должна быть числом", Toast.LENGTH_SHORT).show()
            return false
        }

        val selectedSubtype = subtypes.getOrNull(binding.spinnerPropertyType.selectedItemPosition)
        if (selectedSubtype == null) {
            setErrorStyleSpinner()
            Toast.makeText(requireContext(), "Выберите подтип недвижимости", Toast.LENGTH_SHORT).show()
            return false
        } else {
            resetStyleSpinner()
        }

        if (!isValid) {
            Toast.makeText(requireContext(), "Пожалуйста, заполните обязательные поля", Toast.LENGTH_SHORT).show()
            return false
        }

        val floor = binding.numberEtaj.text.toString().trim().toIntOrNull()
        val totalFloors = binding.kolEtaj.text.toString().trim().toIntOrNull()
        val yearBuilt = binding.godPostroyki.text.toString().trim().toIntOrNull()
        val cadastralNumber = binding.kadastroviyNomer.text.toString().trim().ifEmpty { null }

        viewModel.propertyInfo.value = PropertyInfo(
            propertyType = propertyType,
            subCategory = selectedSubtype,
            region = region,
            address = address,
            cadastralNumber = cadastralNumber,
            totalArea = totalArea,
            floor = floor,
            floorsTotal = totalFloors,
            yearBuilt = yearBuilt
        )

        return true
    }

    private fun setErrorStyle(view: EditText) {
        view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.button_exit))
    }

    private fun resetStyle(view: EditText) {
        view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.back_edittext)
    }

    private fun setErrorStyleSpinner() {
        binding.spinnerPropertyType.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.button_exit))
    }

    private fun resetStyleSpinner() {
        binding.spinnerPropertyType.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.back_edittext))
    }

    private fun restoreSavedInputs() {
        viewModel.propertyInfo.value?.let { property ->
            binding.region.setText(property.region)
            binding.adresNedvig.setText(property.address)
            binding.obchaiaPlochad.setText(property.totalArea.toString())
            getTextWithoutSpaces(binding.obchaiaPlochad)
            binding.kolEtaj.setText(property.floorsTotal?.toString() ?: "")
            binding.numberEtaj.setText(property.floor?.toString() ?: "")
            binding.godPostroyki.setText(property.yearBuilt?.toString() ?: "")
            binding.kadastroviyNomer.setText(property.cadastralNumber ?: "")

            val selected = property.subCategory
            val index = subtypes.indexOf(selected)
            if (index >= 0) {
                binding.spinnerPropertyType.setSelection(index)
                viewModel.selectedSubtype.value = selected
                viewModel.selectedSubtypeIndex.value = index
            }
        }
    }

    fun setFormattedTextWatcher(editText: EditText, type: String) {
        editText.addTextChangedListener(object : TextWatcher {
            var isFormatting = false  // Флаг, чтобы предотвратить бесконечную рекурсию

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
                    var input = editable.toString()  // Убираем пробелы перед обработкой
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
                                // Форматирование даты XX.XX.XXXX
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
                            "cadaster_number" -> {
                                // Форматирование кадастрового номера XX:XX:XXXX:XXXX
                                val maxLength = 20
                                if (input.length > maxLength) {
                                    input = input.substring(0, maxLength)
                                }
                                // Форматирование кадастрового номера с вставками ":"
                                input = input.replace(":", "")  // Убираем все символы ":"
                                if (input.length > 2) {
                                    input = input.substring(0, 2) + ":" + input.substring(2)
                                }
                                if (input.length > 5) {
                                    input = input.substring(0, 5) + ":" + input.substring(5)
                                }
                                if (input.length > 12) {
                                    input = input.substring(0, 12) + ":" + input.substring(12)
                                }
                            }
                        }
                    }
                    // Если текст изменился, обновляем содержимое EditText
                    if (editable.toString() != input) {
                        isFormatting = true
                        editText.removeTextChangedListener(this)  // Отключаем обработчик временно
                        editText.setText(input)
                        editText.setSelection(input.length) // Устанавливаем курсор в конец текста
                        editText.addTextChangedListener(this)  // Включаем обработчик снова
                        isFormatting = false
                    }
                }
            }
        })
    }

    fun setNumericFormattedTextWatcher(editText: EditText, maxLength: Int) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Не обновляем текст здесь, чтобы избежать бесконечной рекурсии
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Ничего не делаем в процессе изменения текста
            }

            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    var input = editable.toString().replace("[^\\d]".toRegex(), "") // Убираем все символы, кроме цифр

                    // Ограничиваем количество символов
                    if (input.length > maxLength) {
                        input = input.substring(0, maxLength)
                    }

                    // Форматирование в зависимости от длины числа
                    val formattedInput = StringBuilder()
                    var count = 0

                    // Делаем форматирование в зависимости от длины числа
                    when {
                        input.length <= 3 -> {
                            // Если длина <= 3: XXX
                            formattedInput.append(input)
                        }
                        input.length in 4..4 -> {
                            // Если длина == 4: X XXX
                            formattedInput.append(input.substring(0, 1)).append(" ").append(input.substring(1))
                        }
                        input.length in 5..5 -> {
                            // Если длина == 5: XX XXX
                            formattedInput.append(input.substring(0, 2)).append(" ").append(input.substring(2))
                        }
                        input.length in 6..6 -> {
                            // Если длина == 6: XXX XXX
                            formattedInput.append(input.substring(0, 3)).append(" ").append(input.substring(3))
                        }
                        input.length in 7..7 -> {
                            // Если длина == 7: X XXX XXX
                            formattedInput.append(input.substring(0, 1)).append(" ").append(input.substring(1, 4)).append(" ").append(input.substring(4))
                        }
                        input.length in 8..8 -> {
                            // Если длина == 8: XX XXX XXX
                            formattedInput.append(input.substring(0, 2)).append(" ").append(input.substring(2, 5)).append(" ").append(input.substring(5))
                        }
                        input.length > 8 -> {
                            // Для всех длин > 8, можно продолжить добавление пробела после каждых 3 цифр
                            for (i in input.indices) {
                                formattedInput.append(input[i])
                                count++
                                if (count % 3 == 0 && count != input.length) {
                                    formattedInput.append(" ") // Добавляем пробел после каждых 3 цифр
                                }
                            }
                        }
                    }

                    // Если текст изменился, обновляем содержимое EditText
                    if (editable.toString() != formattedInput.toString()) {
                        editText.removeTextChangedListener(this)  // Отключаем обработчик временно
                        editText.setText(formattedInput.toString())
                        editText.setSelection(formattedInput.length) // Устанавливаем курсор в конец текста
                        editText.addTextChangedListener(this)  // Включаем обработчик снова
                    }
                }
            }
        })
    }
    fun getTextWithoutSpaces(editText: EditText): String {
        return editText.text.toString().replace(" ", "")
    }
}
