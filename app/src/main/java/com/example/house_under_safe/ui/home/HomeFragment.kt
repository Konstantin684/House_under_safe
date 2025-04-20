package com.example.house_under_safe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.house_under_safe.R
import com.example.house_under_safe.databinding.FragmentHomeBinding
import com.example.house_under_safe.ui.MainSharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MainSharedViewModel by activityViewModels()
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerViewHome
        val placeholder: View = view.findViewById(R.id.placeholder_home_fragment)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HomeAdapter(emptyList())
        recyclerView.adapter = adapter

        sharedViewModel.policies.observe(viewLifecycleOwner, Observer { items ->
            if (items.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                placeholder.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                placeholder.visibility = View.GONE
                adapter.updateItems(items)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val placeholder: View = requireView().findViewById(R.id.placeholder_home_fragment)

        sharedViewModel.policies.value?.let {
            adapter.updateItems(it)
            binding.recyclerViewHome.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            placeholder.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
