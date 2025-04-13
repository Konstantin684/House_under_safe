package com.example.house_under_safe.ui.compensation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentCompensationBinding
import com.example.house_under_safe.ui.home.HomeItem
import com.example.house_under_safe.ui.home.RiskType
import com.example.house_under_safe.ui.home.mockHomeItems

class CompensationFragment : Fragment() {

    private var _binding: FragmentCompensationBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CompensationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompensationBinding.inflate(inflater, container, false)

        val placeholder = binding.root.findViewById<View>(R.id.placeholder_empty_compensation)
        val dummyList = mockHomeItems // или заполни фейковыми данными

        if (dummyList.isEmpty()) {
            binding.recyclerViewCompensation.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        } else {
            binding.recyclerViewCompensation.visibility = View.VISIBLE
            placeholder.visibility = View.GONE

            adapter = CompensationAdapter(dummyList)
            binding.recyclerViewCompensation.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewCompensation.adapter = adapter
        }


        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}



