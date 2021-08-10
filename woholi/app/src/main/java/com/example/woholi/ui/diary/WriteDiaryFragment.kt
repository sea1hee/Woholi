package com.example.woholi.ui.diary

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.R
import com.example.woholi.adapter.AddPhotoAdapter
import com.example.woholi.databinding.FragmentDiaryBinding
import com.example.woholi.databinding.FragmentWriteDiaryBinding
import com.example.woholi.db.DiaryViewModel
import com.example.woholi.model.Diary
import com.example.woholi.ui.MainActivity


class WriteDiaryFragment : Fragment() {

    private lateinit var binding: FragmentWriteDiaryBinding

    val diaryVM by viewModels<DiaryViewModel>({ requireActivity() })

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

        val adapter: AddPhotoAdapter = AddPhotoAdapter()
        binding.recyclerViewPhoto.adapter = adapter
        binding.recyclerViewPhoto.layoutManager = LinearLayoutManager(requireContext()).also { it.orientation = LinearLayoutManager.HORIZONTAL }

        binding.btnBack.setOnClickListener {
            (activity as MainActivity).setFlag(7)
        }

        binding.btnConfirm.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val contents = binding.edtContents.text.toString()
            if (title == "" || contents == "") {
                Toast.makeText(context, "입력해주세요.", Toast.LENGTH_SHORT)
            } else {
                diaryVM.writeDiary(Diary(curDay, title, contents, arrayListOf("https://i.ytimg.com/vi/IT5Uq2K05C0/default.jpg")))
                binding.btnBack.callOnClick()
            }
        }

    }
}