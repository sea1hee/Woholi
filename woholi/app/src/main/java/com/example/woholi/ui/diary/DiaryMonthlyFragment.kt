package com.example.woholi.ui.diary

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woholi.R
import com.example.woholi.databinding.FragmentDiaryMonthlyBinding
import com.example.woholi.model.Check
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.InputStream

class DiaryMonthlyFragment : Fragment() {

    val TAG = "Monthly"
    private lateinit var binding: FragmentDiaryMonthlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryMonthlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calenMonthly.state().edit()
                .setMaximumDate(CalendarDay.today())
                .commit()


        binding.calenMonthly.setDateSelected(CalendarDay.today(), true) // 기본으로 오늘 날짜 선택됨
        binding.calenMonthly.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
        binding.calenMonthly.isClickable = true
        binding.calenMonthly.setOnDateChangedListener { widget, date, selected ->
            //if 해당 날짜의 데이터없으면 write
            (activity as MainActivity).setFlag(5)
            // 있으면 daily
            //(activity as MainActivity).setFlag(6)
        }
    }
}