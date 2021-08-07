package com.example.woholi.ui.diary

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.woholi.R
import com.example.woholi.adapter.ViewPagerAdapter
import com.example.woholi.databinding.FragmentDiaryBinding
import com.example.woholi.db.DiaryViewModel
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import java.net.URL

class DiaryFragment : Fragment() {

    private lateinit var binding : FragmentDiaryBinding
    val diaryVM by viewModels<DiaryViewModel>({requireActivity()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)

        setWeekly()

        binding.tgMonthly.setOnClickListener {
            if (binding.tgMonthly.isChecked) setMonthly() else setWeekly()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendar.setOnDateChangedListener { widget, date, selected ->

            val curDay = diaryVM.changeFormatDate(date)

            if (diaryVM.existDate(date)) {
                (activity as MainActivity).setFlag(6, curDay)
            }
            else{
                (activity as MainActivity).setFlag(5, curDay)
            }
        }

        diaryVM.diaryList.observe(viewLifecycleOwner) {
            Log.d("Repository", "Read Diary")
            for (i in 0..diaryVM.DiaryList.size-1) {
                val curDate = diaryVM.DiaryList[i].date

                val year: Int = curDate.substring(0 until 4).toInt()
                val month: Int = curDate.substring(4 until 6).toInt()
                val date: Int = curDate.substring(6 until 8).toInt()
                val calDay = CalendarDay.from(year, month, date)
                CoroutineScope(Dispatchers.IO).async {
                    val iStream = URL(diaryVM.DiaryList[i].url[0]).content as InputStream
                    val curImage: Drawable = Drawable.createFromStream(iStream, curDate)

                    withContext(Dispatchers.Main) {
                        binding.calendar.addDecorator(
                                CurrentDayDecorator(
                                        requireActivity(),
                                        calDay,
                                        curImage
                                )
                        )
                    }
                }
            }
        }


    }

    fun setViewPager(){
        binding.viewpagerWeekly.isVisible = true
        val fragmentList = mutableListOf<Fragment>()
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.fragmentList = fragmentList
        binding.viewpagerWeekly.adapter = adapter


        binding.viewpagerWeekly.onFocusChangeListener = View.OnFocusChangeListener{ view, hasFocus ->
            if (hasFocus){
                binding.calendar.clearSelection()
                binding.calendar.setCurrentDate(CalendarDay.today(), true)
                binding.calendar.setDateSelected(CalendarDay.today(), true)
            }
        }
    }

    fun setWeekly(){
        binding.calendar.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setMaximumDate(CalendarDay.today())
                .commit()

        setViewPager()

        binding.calendar.topbarVisible = false
        binding.calendar.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
        binding.calendar.isClickable = true
        binding.calendar.setDateSelected(CalendarDay.today(), true) // 기본으로 오늘 날짜 선택됨
    }

    fun setMonthly(){
        binding.calendar.state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

        binding.viewpagerWeekly.isVisible = false
        binding.calendar.topbarVisible = true
    }

}