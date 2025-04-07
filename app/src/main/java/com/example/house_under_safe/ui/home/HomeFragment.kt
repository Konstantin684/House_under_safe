package com.example.house_under_safe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val listView = binding.listViewHome
        val placeholder = root.findViewById<View>(R.id.placeholder_empty_home)

        // Здесь можно подключиться к данным ViewModel (пока эмулируем список)
        val items = listOf<String>() // или, например: listOf("Полис 1", "Полис 2")

        if (items.isEmpty()) {
            listView.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        } else {
            listView.visibility = View.VISIBLE
            placeholder.visibility = View.GONE

            listView.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                items
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
