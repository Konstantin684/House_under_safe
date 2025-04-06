package com.example.house_under_safe.ui.management

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
            ManagementItem(R.drawable.gor_jil_ned, "Городская жилая недвижимость", "Оформить страховой полис для городской жилой недвижимости (квартира, комната в коммунальной квартире)"),
            ManagementItem(R.drawable.zagor_jil_ned, "Загородная жилая недвижимость", "Оформить страховой полис для загородной жилой недвижимости (дачные дома с возможностью регистрации, жилые дома)"),
            ManagementItem(R.drawable.gor_nejil_ned, "Городская нежилая недвижимость", "Оформить страховой полис для городской нежилой недвижимости (апартаменты)"),
            ManagementItem(R.drawable.komer_ned, "Комерческая недвижимость", "Оформить страховой полис для коммерческой недвижимости (склады, офисные помещения, гостиницы, отели, мотели, торговые помещения)"),
            ManagementItem(R.drawable.prom_ned, "Промышленная недвижимость", "Оформить страховой полис для промышленной недвижимости (промышленные сооружения, работающие заводы, фабрики)"),
            ManagementItem(R.drawable.prodl_polis, "Продление полиса", "Продлить срок действия уже оформленного страхового полиса на недвижимость")
        )

        adapter = ManagementAdapter(requireContext(), items)
        listView.adapter = adapter

        return view
    }
}