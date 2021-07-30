package com.example.woholi.ui.diary

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.woholi.databinding.FragmentDiaryWeeklyBinding
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calenWeekly.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setMaximumDate(CalendarDay.today())
                .commit()

        binding.calenWeekly.topbarVisible = false
        binding.calenWeekly.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
        binding.calenWeekly.isClickable = true
        binding.calenWeekly.setOnDateChangedListener { widget, date, selected ->

            val curMonth = if (date.month < 10) "0${date.month}" else date.month.toString()
            val curDate = if (date.day < 10) "0${date.day}" else date.day.toString()
            val curDay = "${date.year}${curMonth}${curDate}"

            Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("diary").document(curDay).get()
                .addOnSuccessListener { document ->
                    if (document.exists()){
                        (activity as MainActivity).setFlag(6, "${date.year}${curMonth}${curDate}")
                    }
                    else{
                        (activity as MainActivity).setFlag(5, "${date.year}${curMonth}${curDate}")
                    }

                }

        }

        binding.button.setOnClickListener {
            binding.calenWeekly.clearSelection()
            binding.calenWeekly.setCurrentDate(CalendarDay.today(), true)
            binding.calenWeekly.setDateSelected(CalendarDay.today(), true)
        }
    }


    suspend fun existDiary(curDay: String): DocumentReference {
        val docRef = Firebase.firestore.collection("users").document(CurrentUser.uid)
            .collection("diary").document(curDay)
        return docRef
    }
}