package com.example.house_under_safe.ui.compensation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.databinding.FragmentCompensationBinding

class CompensationFragment : Fragment() {

    private var _binding: FragmentCompensationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val compensationViewModel =
            ViewModelProvider(this).get(CompensationViewModel::class.java)

        _binding = FragmentCompensationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCompensation
        compensationViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}