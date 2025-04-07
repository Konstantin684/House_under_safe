package com.example.house_under_safe.ui.compensation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentCompensationBinding

class CompensationFragment : Fragment() {

    private var _binding: FragmentCompensationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompensationBinding.inflate(inflater, container, false)
        val root = binding.root

        val compensationViewModel = ViewModelProvider(this)[CompensationViewModel::class.java]

        val listView = binding.listViewCompensation
        val placeholder = root.findViewById<View>(R.id.placeholder_empty_compensation)

        // Пока заглушка, позже можно заменить на данные из ViewModel
        val items = listOf<String>() // например: listOf("Заявка 1", "Заявка 2")

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
