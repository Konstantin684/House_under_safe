package com.example.house_under_safe.policy_design.step_3

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.model.InsuranceConditions
import com.example.house_under_safe.model.InsuranceRisk
import com.example.house_under_safe.model.PaymentFrequency
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

        // Восстановление сохранённых данных
        viewModel.insuranceConditions.value?.let { saved ->
            konstrukcia.setText(saved.constructionCost.toString())
            otdelka.setText(saved.finishingCost?.toString() ?: "")
            tehnicheskoe.setText(saved.equipmentCost?.toString() ?: "")
            dvigimoe.setText(saved.movablePropertyCost?.toString() ?: "")
            gragdanskaya.setText(saved.civilLiabilityCost?.toString() ?: "")
            seekBar.progress = (saved.insuranceTermInYears - 1).coerceIn(0, 9)
            durationText.text = "${saved.insuranceTermInYears} ${getYearEnding(saved.insuranceTermInYears)}"

            radioMap.entries.find { it.value == saved.paymentFrequency }?.key?.let {
                radioGroup.check(it)
            }

            checkboxMap.forEach { (checkId, risk) ->
                view.findViewById<CheckBox>(checkId).isChecked = saved.risks.contains(risk)
            }
        }

        view.findViewById<ImageButton>(R.id.imageButton4).setOnClickListener {
            val structure = konstrukcia.text.toString().toDoubleOrNull()
            if (structure == null) {
                Toast.makeText(requireContext(), "Введите сумму конструкции", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedFrequency = radioMap[radioGroup.checkedRadioButtonId]
            if (selectedFrequency == null) {
                Toast.makeText(requireContext(), "Выберите частоту платежей", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val insuranceConditions = InsuranceConditions(
                constructionCost = structure,
                finishingCost = otdelka.text.toString().toDoubleOrNull(),
                equipmentCost = tehnicheskoe.text.toString().toDoubleOrNull(),
                movablePropertyCost = dvigimoe.text.toString().toDoubleOrNull(),
                civilLiabilityCost = gragdanskaya.text.toString().toDoubleOrNull(),
                insuranceTermInYears = seekBar.progress + 1,
                paymentFrequency = selectedFrequency,
                risks = checkboxMap.filter { (id, _) ->
                    view.findViewById<CheckBox>(id).isChecked
                }.values.toList()
            )

            viewModel.insuranceConditions.value = insuranceConditions
            (requireActivity() as? DesignPolicyActivity)?.updateProgress(3)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, FourthStepFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            (requireActivity() as? DesignPolicyActivity)?.updateProgress(1)
        }
    }

    private fun getYearEnding(years: Int): String = when (years) {
        1 -> "год"
        2, 3, 4 -> "года"
        else -> "лет"
    }
}
