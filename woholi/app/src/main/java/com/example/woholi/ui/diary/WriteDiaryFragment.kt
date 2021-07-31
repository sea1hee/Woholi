package com.example.woholi.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.R
import com.example.woholi.adapter.AddPhotoAdapter
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

        val curDay = arguments?.getString("curDay")
        binding.txSelecteddate.text = "${curDay!!.substring(0 until 4)}.${curDay!!.substring(4 until 6)}.${curDay!!.substring(6 until 8)}"

        val adapter : AddPhotoAdapter = AddPhotoAdapter()
        binding.recyclerViewPhoto.adapter = adapter
        binding.recyclerViewPhoto.layoutManager = LinearLayoutManager(requireContext()).also {it.orientation = LinearLayoutManager.HORIZONTAL}

        binding.btnBack.setOnClickListener {
            (activity as MainActivity).setFlag(7)
        }
    }

}