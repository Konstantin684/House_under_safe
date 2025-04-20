package com.example.house_under_safe.ui.management

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentManagementBinding
import com.example.house_under_safe.model.PropertyType
import com.example.house_under_safe.policy_design.DesignPolicyActivity

class ManagementFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ManagementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_management, container, false)

        listView = view.findViewById(R.id.management_list)

        val items = listOf(
            ManagementItem(
                R.drawable.gor_jil_ned,
                "Городская жилая недвижимость",
                "Оформить страховой полис для городской жилой недвижимости (квартира, комната в коммунальной квартире)",
                PropertyType.CityResidential
            ),
            ManagementItem(
                R.drawable.zagor_jil_ned,
                "Загородная жилая недвижимость",
                "Оформить страховой полис для загородной жилой недвижимости (дачные дома, жилые дома)",
                PropertyType.CountryResidential
            ),
            ManagementItem(
                R.drawable.gor_nejil_ned,
                "Городская нежилая недвижимость",
                "Оформить страховой полис для городской нежилой недвижимости (апартаменты)",
                PropertyType.CountryNotResidential
            ),
            ManagementItem(
                R.drawable.komer_ned,
                "Коммерческая недвижимость",
                "Оформить страховой полис для коммерческой недвижимости (склады, гостиницы и т.п.)",
                PropertyType.Commercial
            ),
            ManagementItem(
                R.drawable.prom_ned,
                "Промышленная недвижимость",
                "Оформить страховой полис для промышленной недвижимости (заводы, фабрики и т.п.)",
                PropertyType.Industrial
            ),
            ManagementItem(
                R.drawable.prodl_polis,
                "Продление полиса",
                "Продлить срок действия уже оформленного страхового полиса на недвижимость",
                PropertyType.ProdleniePolisa
            )
        )

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]

            val intent = Intent(requireContext(), DesignPolicyActivity::class.java).apply {
                putExtra("property_type", selectedItem.propertyType::class.simpleName)
            }
            startActivity(intent)
        }

        adapter = ManagementAdapter(requireContext(), items)
        listView.adapter = adapter

        return view
    }
}