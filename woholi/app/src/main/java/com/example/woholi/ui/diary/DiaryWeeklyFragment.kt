package com.example.woholi.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.databinding.FragmentDiaryWeeklyBinding
import com.example.woholi.ui.MainActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar


class DiaryWeeklyFragment : Fragment() {

    private lateinit var binding: FragmentDiaryWeeklyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calenWeekly.state().edit()
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .setMaximumDate(CalendarDay.today())
            .commit()

        binding.calenWeekly.topbarVisible = false
        binding.calenWeekly.setDateSelected(CalendarDay.today(), true)
        binding.calenWeekly.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
        binding.calenWeekly.isClickable = true
        binding.calenWeekly.setOnDateChangedListener { widget, date, selected ->
            //if 해당 날짜의 데이터없으면 write
            //(activity as MainActivity).setFlag(5)
            // 있으면 daily
            (activity as MainActivity).setFlag(6)

        }
    }
}