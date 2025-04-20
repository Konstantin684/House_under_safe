package com.example.house_under_safe.policy_design.step_1

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentFirstStepBinding
import com.example.house_under_safe.model.PropertyInfo
import com.example.house_under_safe.model.PropertyType
import com.example.house_under_safe.policy_design.DesignPolicyActivity
import com.example.house_under_safe.policy_design.PolicyDesignViewModel
import com.example.house_under_safe.policy_design.step_2.SecondStepFragment

class FirstStepFragment : Fragment(R.layout.fragment_first_step) {

    private lateinit var binding: FragmentFirstStepBinding
    private lateinit var viewModel: PolicyDesignViewModel
    private lateinit var subtypes: Array<out Enum<*>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFirstStepBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[PolicyDesignViewModel::class.java]
        restoreSavedInputs()

        // Обработка кнопки "Далее"
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
                is PropertyType.CityResidential -> PropertyType.CityResidential.Subtype.values()
                is PropertyType.CountryResidential -> PropertyType.CountryResidential.Subtype.values()
                is PropertyType.CountryNotResidential -> PropertyType.CountryNotResidential.Subtype.values()
                is PropertyType.Commercial -> PropertyType.Commercial.Subtype.values()
                is PropertyType.Industrial -> PropertyType.Industrial.Subtype.values()
                is PropertyType.ProdleniePolisa -> PropertyType.ProdleniePolisa.Subtype.values()
            }

            val subtypeLabels = subtypes.map {
                when (it) {
                    is PropertyType.CityResidential.Subtype -> it.label
                    is PropertyType.CountryResidential.Subtype -> it.label
                    is PropertyType.CountryNotResidential.Subtype -> it.label
                    is PropertyType.Commercial.Subtype -> it.label
                    is PropertyType.Industrial.Subtype -> it.label
                    else -> it.name // на всякий случай
                }
            }


            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subtypeLabels)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPropertyType.adapter = adapter

            binding.spinnerPropertyType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
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

        val regionEdit = binding.region
        val addressEdit = binding.adresNedvig
        val areaEdit = binding.obchaiaPlochad
        val floorEdit = binding.numberEtaj
        val totalFloorsEdit = binding.kolEtaj
        val yearBuiltEdit = binding.godPostroyki
        val cadastralEdit = binding.kadastroviyNomer
        val spinner = binding.spinnerPropertyType

        val region = regionEdit.text.toString().trim()
        val address = addressEdit.text.toString().trim()
        val areaText = areaEdit.text.toString().trim()
        val floorText = floorEdit.text.toString().trim()
        val totalFloorsText = totalFloorsEdit.text.toString().trim()
        val yearBuiltText = yearBuiltEdit.text.toString().trim()
        val cadastralNumber = cadastralEdit.text.toString().trim().ifEmpty { null }

        val selectedPosition = binding.spinnerPropertyType.selectedItemPosition
        val selectedSubtypeEnum = if (selectedPosition >= 0 && selectedPosition < subtypes.size) {
            subtypes[selectedPosition]
        } else null


        var isValid = true

        if (region.isEmpty()) {
            setErrorStyle(regionEdit); isValid = false
        } else resetStyle(regionEdit)
        if (address.isEmpty()) {
            setErrorStyle(addressEdit); isValid = false
        } else resetStyle(addressEdit)
        if (areaText.isEmpty()) {
            setErrorStyle(areaEdit); isValid = false
        } else resetStyle(areaEdit)

        val totalArea = areaText.toDoubleOrNull()
        if (totalArea == null) {
            setErrorStyle(areaEdit)
            Toast.makeText(requireContext(), "Площадь должна быть числом", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (selectedSubtypeEnum == null) {
            setErrorStyleSpinner()
            Toast.makeText(requireContext(), "Выберите подтип недвижимости", Toast.LENGTH_SHORT)
                .show()
            return false
        } else {
            resetStyleSpinner()
        }

        if (!isValid) {
            Toast.makeText(
                requireContext(),
                "Пожалуйста, заполните обязательные поля",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        val floor = floorText.toIntOrNull()
        val totalFloors = totalFloorsText.toIntOrNull()
        val yearBuilt = yearBuiltText.toIntOrNull()

        val propertyInfo = PropertyInfo(
            region = region,
            propertyType = propertyType,
            subCategory = selectedSubtypeEnum,
            address = address,
            cadastralNumber = cadastralNumber,
            totalArea = totalArea,
            floor = floor,
            floorsTotal = totalFloors,
            yearBuilt = yearBuilt
        )

        viewModel.propertyInfo.value = propertyInfo
        return true
    }

    private fun setErrorStyle(view: EditText) {
        view.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.button_exit))
    }

    private fun resetStyle(view: EditText) {
        view.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.back_edittext)
    }

    private fun setErrorStyleSpinner() {
        binding.spinnerPropertyType.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.button_exit))
    }

    private fun resetStyleSpinner() {
        binding.spinnerPropertyType.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.back_edittext))
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

            // Восстанавливаем подтип, если он есть
            val selected = property.subCategory
            selected?.let {
                val index = subtypes.indexOf(it)
                if (index >= 0) {
                    binding.spinnerPropertyType.setSelection(index)
                    viewModel.selectedSubtype.value = it
                    viewModel.selectedSubtypeIndex.value = index
                }
            }
        }
    }
}
