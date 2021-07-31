package com.example.woholi.ui.diary

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.example.woholi.adapter.ViewPagerAdapter
import com.example.woholi.databinding.FragmentDiaryWeeklyBinding
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.MainActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView


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

        val fragmentList = mutableListOf<Fragment>()
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.fragmentList = fragmentList
        binding.viewpagerWeekly.adapter = adapter


        binding.viewpagerWeekly.onFocusChangeListener = View.OnFocusChangeListener{ view, hasFocus ->
            if (hasFocus){
                binding.calenWeekly.clearSelection()
                binding.calenWeekly.setCurrentDate(CalendarDay.today(), true)
                binding.calenWeekly.setDateSelected(CalendarDay.today(), true)
            }
        }
    }


    suspend fun existDiary(curDay: String): DocumentReference {
        val docRef = Firebase.firestore.collection("users").document(CurrentUser.uid)
            .collection("diary").document(curDay)
        return docRef
    }
}

