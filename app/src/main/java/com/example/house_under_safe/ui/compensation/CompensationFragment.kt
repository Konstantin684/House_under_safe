package com.example.house_under_safe.ui.compensation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.house_under_safe.PolicyStorageHelper
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentCompensationBinding
import com.example.house_under_safe.model.InsurancePolicy
import com.example.house_under_safe.model.PropertyType
import com.example.house_under_safe.ui.home.HomeItemUiModel
import com.example.house_under_safe.ui.home.InsuranceRiskUi
import com.example.house_under_safe.ui.home.insuranceRiskIcons

class CompensationFragment : Fragment() {

    private var _binding: FragmentCompensationBinding? = null
    private val binding get() = _binding!!
    private lateinit var placeholder: View
    private lateinit var adapter: CompensationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompensationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeholder = view.findViewById(R.id.placeholder_empty_compensation)

        binding.recyclerViewCompensation.layoutManager = LinearLayoutManager(requireContext())
        adapter = CompensationAdapter(emptyList())
        binding.recyclerViewCompensation.adapter = adapter

        loadAndDisplayPolicies()
    }

    override fun onResume() {
        super.onResume()
        loadAndDisplayPolicies()
    }

    private fun loadAndDisplayPolicies() {
        val policies: List<InsurancePolicy> = PolicyStorageHelper.loadFullPolicies(requireContext())
        Log.d("CompensationFragment", "Загружено из файла: ${policies.size} полисов")

        if (policies.isEmpty()) {
            binding.recyclerViewCompensation.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        } else {
            val uiModels = policies.map { policy ->
                HomeItemUiModel(
                    planResId = getPlanIcon(policy.property.propertyType),
                    policyNumber = policy.policyNumber,
                    region = policy.property.region,
                    propertyType = policy.property.propertyType,
                    subtype = policy.property.subCategory,
                    address = policy.property.address,
                    period = "${policy.startDate} – ${policy.endDate}",
                    risks = policy.risks.map {
                        InsuranceRiskUi(
                            risk = it,
                            iconRes = insuranceRiskIcons[it] ?: R.drawable.unchecked_profile
                        )
                    }
                )
            }

            binding.recyclerViewCompensation.visibility = View.VISIBLE
            placeholder.visibility = View.GONE
            adapter.updateItems(uiModels)
        }
    }

    private fun getPlanIcon(propertyType: PropertyType): Int {
        return when (propertyType) {
            is PropertyType.CityResidential -> R.drawable.gor_jil_ned
            is PropertyType.CountryResidential -> R.drawable.zagor_jil_ned
            is PropertyType.CountryNotResidential -> R.drawable.gor_nejil_ned
            is PropertyType.Commercial -> R.drawable.komer_ned
            is PropertyType.Industrial -> R.drawable.prom_ned
            PropertyType.ProdleniePolisa -> R.drawable.unchecked_profile
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



