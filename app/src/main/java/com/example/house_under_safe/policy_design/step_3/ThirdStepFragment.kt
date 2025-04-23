package com.example.house_under_safe.policy_design.step_3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.model.*
import com.example.house_under_safe.policy_design.DesignPolicyActivity
import com.example.house_under_safe.policy_design.PolicyDesignViewModel
import com.example.house_under_safe.policy_design.step_4.FourthStepFragment

class ThirdStepFragment : Fragment(R.layout.fragment_third_step) {

    private lateinit var viewModel: PolicyDesignViewModel
    private lateinit var seekBar: SeekBar
    private lateinit var durationText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PolicyDesignViewModel::class.java]

        val konstrukcia = view.findViewById<EditText>(R.id.konstrukcia)
        val otdelka = view.findViewById<EditText>(R.id.otdelka)
        val tehnicheskoe = view.findViewById<EditText>(R.id.tehnicheskoe_oborudovanie)
        val dvigimoe = view.findViewById<EditText>(R.id.dvigimoe_imuchestvo)
        val gragdanskaya = view.findViewById<EditText>(R.id.gragdanskaya_otvetstvennost)

        setNumericFormattedTextWatcher(konstrukcia, 24)
        setNumericFormattedTextWatcher(otdelka, 24)
        setNumericFormattedTextWatcher(tehnicheskoe, 24)
        setNumericFormattedTextWatcher(dvigimoe, 24)
        setNumericFormattedTextWatcher(gragdanskaya, 24)

        val checkboxMap = mapOf(
            R.id.checkBox2 to InsuranceRisk.FIRE,
            R.id.checkBox3 to InsuranceRisk.ELEMENT,
            R.id.checkBox4 to InsuranceRisk.VANDALISM,
            R.id.checkBox5 to InsuranceRisk.FLOOD,
            R.id.checkBox6 to InsuranceRisk.ROBBERY
        )

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val radioMap = mapOf(
            R.id.radioButton1 to PaymentFrequency.ONE_TIME,
            R.id.radioButton2 to PaymentFrequency.MONTHLY,
            R.id.radioButton3 to PaymentFrequency.QUARTERLY,
            R.id.radioButton4 to PaymentFrequency.YEARLY
        )

        seekBar = view.findViewById(R.id.seekBar)
        durationText = view.findViewById(R.id.textView13)
        seekBar.max = 9

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val years = progress + 1
                durationText.text = "$years ${getYearEnding(years)}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        restoreInputs(view, checkboxMap, radioMap)

        view.findViewById<ImageButton>(R.id.imageButton4).setOnClickListener {
            handleNextClick(view, konstrukcia, otdelka, tehnicheskoe, dvigimoe, gragdanskaya, checkboxMap, radioGroup, radioMap)
        }

        view.findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            (requireActivity() as? DesignPolicyActivity)?.updateProgress(1)
        }
    }

    private fun handleNextClick(
        view: View,
        konstrukcia: EditText,
        otdelka: EditText,
        tehnicheskoe: EditText,
        dvigimoe: EditText,
        gragdanskaya: EditText,
        checkboxMap: Map<Int, InsuranceRisk>,
        radioGroup: RadioGroup,
        radioMap: Map<Int, PaymentFrequency>
    ) {
        // Очистка пробелов и преобразование в число
        val structure = getTextWithoutSpaces(konstrukcia).toDoubleOrNull()
        if (structure == null) {
            Toast.makeText(requireContext(), "Введите сумму конструкции", Toast.LENGTH_SHORT).show()
            return
        }
        // Получаем и проверяем выбранную частоту платежей
        val selectedFrequency = radioMap[radioGroup.checkedRadioButtonId]
        if (selectedFrequency == null) {
            Toast.makeText(requireContext(), "Выберите частоту платежей", Toast.LENGTH_SHORT).show()
            return
        }
        // Получаем остальные значения, очищаем пробелы и преобразуем в Double
        val finishingCost = getTextWithoutSpaces(otdelka).toDoubleOrNull()
        val equipmentCost = getTextWithoutSpaces(tehnicheskoe).toDoubleOrNull()
        val movablePropertyCost = getTextWithoutSpaces(dvigimoe).toDoubleOrNull()
        val civilLiabilityCost = getTextWithoutSpaces(gragdanskaya).toDoubleOrNull()

        // Создаем объект InsuranceConditions с проверенными значениями
        val insuranceConditions = InsuranceConditions(
            constructionCost = structure,
            finishingCost = finishingCost,
            equipmentCost = equipmentCost,
            movablePropertyCost = movablePropertyCost,
            civilLiabilityCost = civilLiabilityCost,
            insuranceTermInYears = seekBar.progress + 1,
            paymentFrequency = selectedFrequency,
            risks = checkboxMap.filter { view.findViewById<CheckBox>(it.key).isChecked }.values.toList()
        )
        // Сохраняем данные в ViewModel
        viewModel.insuranceConditions.value = insuranceConditions
        // Обновляем прогресс
        (requireActivity() as? DesignPolicyActivity)?.updateProgress(3)
        // Переход к следующему шагу
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, FourthStepFragment())
            .addToBackStack(null)
            .commit()
    }


    private fun restoreInputs(
        view: View,
        checkboxMap: Map<Int, InsuranceRisk>,
        radioMap: Map<Int, PaymentFrequency>
    ) {
        viewModel.insuranceConditions.value?.let { saved ->
            //очищаем поля от пробелов
            view.findViewById<EditText>(R.id.konstrukcia).setText(getTextWithoutSpaces(view.findViewById(R.id.konstrukcia)))
            view.findViewById<EditText>(R.id.otdelka).setText(getTextWithoutSpaces(view.findViewById(R.id.otdelka)))
            view.findViewById<EditText>(R.id.tehnicheskoe_oborudovanie).setText(getTextWithoutSpaces(view.findViewById(R.id.tehnicheskoe_oborudovanie)))
            view.findViewById<EditText>(R.id.dvigimoe_imuchestvo).setText(getTextWithoutSpaces(view.findViewById(R.id.dvigimoe_imuchestvo)))
            view.findViewById<EditText>(R.id.gragdanskaya_otvetstvennost).setText(getTextWithoutSpaces(view.findViewById(R.id.gragdanskaya_otvetstvennost)))

            // Присваиваем сохраненные данные
            view.findViewById<EditText>(R.id.konstrukcia).setText(saved.constructionCost.toString())
            view.findViewById<EditText>(R.id.otdelka).setText(saved.finishingCost?.toString() ?: "")
            view.findViewById<EditText>(R.id.tehnicheskoe_oborudovanie).setText(saved.equipmentCost?.toString() ?: "")
            view.findViewById<EditText>(R.id.dvigimoe_imuchestvo).setText(saved.movablePropertyCost?.toString() ?: "")
            view.findViewById<EditText>(R.id.gragdanskaya_otvetstvennost).setText(saved.civilLiabilityCost?.toString() ?: "")


            seekBar.progress = (saved.insuranceTermInYears - 1).coerceIn(0, 9)
            durationText.text = "${saved.insuranceTermInYears} ${getYearEnding(saved.insuranceTermInYears)}"

            radioMap.entries.find { it.value == saved.paymentFrequency }?.key?.let {
                view.findViewById<RadioGroup>(R.id.radioGroup).check(it)
            }

            checkboxMap.forEach { (checkId, risk) ->
                view.findViewById<CheckBox>(checkId).isChecked = saved.risks.contains(risk)
            }
        }
    }

    private fun getYearEnding(years: Int): String = when (years) {
        1 -> "год"
        2, 3, 4 -> "года"
        else -> "лет"
    }

    private fun calculatePeriod(years: Int): String {
        val calendar = java.util.Calendar.getInstance()
        val format = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
        val start = format.format(calendar.time)
        calendar.add(java.util.Calendar.YEAR, years)
        val end = format.format(calendar.time)
        return "$start – $end"
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
