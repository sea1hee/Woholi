package com.example.woholi.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.R
import com.example.woholi.databinding.FragmentDiaryBinding
import com.example.woholi.databinding.FragmentWriteDiaryBinding
import com.example.woholi.ui.MainActivity


class WriteDiaryFragment : Fragment() {

    private lateinit var binding : FragmentWriteDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWriteDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            (activity as MainActivity).setFlag(7)
        }

    }

}