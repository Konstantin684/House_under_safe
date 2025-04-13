package com.example.house_under_safe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        val placeholder = root.findViewById<View>(R.id.placeholder_empty_home)
        val recyclerView = binding.recyclerViewHome

        val items = getDummyItems()

        if (items.isEmpty()) {
            recyclerView.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            placeholder.visibility = View.GONE

            adapter = HomeAdapter(items)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }

        return root
    }

    private fun getDummyItems(): List<HomeItem> {
        return listOf(
            /*HomeItem(
                planResId = R.drawable.image_plan,
                numberPolice = 123456,
                locationRegion = "Московская область",
                typeRealEstate = "Квартира",
                adresRealEstate = "ул. Ленина, 10",
                validatyPeriod = "01.01.2024 - 01.01.2025",
                risks = listOf(RiskType.FIRE, RiskType.FLOOD)
            ),
            HomeItem(
                planResId = R.drawable.image_plan,
                numberPolice = 654321,
                locationRegion = "Санкт-Петербург",
                typeRealEstate = "Дом",
                adresRealEstate = "пр. Невский, 50",
                validatyPeriod = "01.02.2024 - 01.02.2025",
                risks = listOf(RiskType.VANDALISM, RiskType.ROBBERY)
            )*/
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
