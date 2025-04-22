package com.example.house_under_safe.policy_design.step_1

import android.content.res.ColorStateList
import android.os.Bundle
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

class FirstStepFragment : Fragment(R.layout.fragment_first_step) {

    private lateinit var binding: FragmentFirstStepBinding
    private lateinit var viewModel: PolicyDesignViewModel
    private lateinit var subtypes: Array<out PropertySubtype>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFirstStepBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[PolicyDesignViewModel::class.java]
        restoreSavedInputs()

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
        val areaText = binding.obchaiaPlochad.text.toString().trim()

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
}
