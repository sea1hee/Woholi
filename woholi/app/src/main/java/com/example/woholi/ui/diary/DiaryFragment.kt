package com.example.woholi.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.R
import com.example.woholi.databinding.FragmentDiaryBinding
import com.example.woholi.ui.MainActivity

class DiaryFragment : Fragment() {

    private lateinit var binding : FragmentDiaryBinding
    private val monthlyFragment by lazy { DiaryMonthlyFragment()}
    private val weeklyFragment by lazy {DiaryWeeklyFragment()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)

        setFragemnt(1)

        binding.tgMonthly.setOnClickListener {
            setFragemnt(if (binding.tgMonthly.isChecked) 0 else 1)
        }
        return binding.root
    }

    fun setFragemnt(flag: Int){
        when(flag){
            0 -> childFragmentManager.beginTransaction().replace(R.id.frameLayout_dairy, monthlyFragment).commit()
            1 -> childFragmentManager.beginTransaction().replace(R.id.frameLayout_dairy, weeklyFragment).commit()

            2 -> (activity as MainActivity).setFlag(5) // write
            3 ->(activity as MainActivity).setFlag(6) // daily
        }
    }

}