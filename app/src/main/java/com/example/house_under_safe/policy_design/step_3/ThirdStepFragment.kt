package com.example.house_under_safe.policy_design.step_3

import android.os.Bundle
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
        val structure = konstrukcia.text.toString().toDoubleOrNull()
        if (structure == null) {
            Toast.makeText(requireContext(), "Введите сумму конструкции", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedFrequency = radioMap[radioGroup.checkedRadioButtonId]
        if (selectedFrequency == null) {
            Toast.makeText(requireContext(), "Выберите частоту платежей", Toast.LENGTH_SHORT).show()
            return
        }

        val insuranceConditions = InsuranceConditions(
            constructionCost = structure,
            finishingCost = otdelka.text.toString().toDoubleOrNull(),
            equipmentCost = tehnicheskoe.text.toString().toDoubleOrNull(),
            movablePropertyCost = dvigimoe.text.toString().toDoubleOrNull(),
            civilLiabilityCost = gragdanskaya.text.toString().toDoubleOrNull(),
            insuranceTermInYears = seekBar.progress + 1,
            paymentFrequency = selectedFrequency,
            risks = checkboxMap.filter { view.findViewById<CheckBox>(it.key).isChecked }.values.toList()
        )

        viewModel.insuranceConditions.value = insuranceConditions

        (requireActivity() as? DesignPolicyActivity)?.updateProgress(3)

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
}
