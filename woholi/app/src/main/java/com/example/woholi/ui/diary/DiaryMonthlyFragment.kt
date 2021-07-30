package com.example.woholi.ui.diary

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
import java.util.*
import kotlin.collections.HashMap

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

        CoroutineScope(Dispatchers.Main).launch {
            val deferred = readDiary().await().documents
            val deffered2 = readPhoto(deferred)
            initUI(deferred, deffered2)
        }
    }

    suspend fun readPhoto(task: MutableList<DocumentSnapshot>): HashMap<String, Drawable> {
        val map: HashMap<String, Drawable> = HashMap()
        withContext(Dispatchers.IO) {
            for (document in task) {
                val url = document.data!!.get("photo").toString()
                val curDate = document.id
                val iStream = java.net.URL(url).content as InputStream
                val curImage: Drawable = Drawable.createFromStream(iStream, curDate)
                map.put(curDate, curImage)
            }
        }
        return map
    }


    suspend fun readDiary(): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("diary").get()
    }

    fun initUI(task: MutableList<DocumentSnapshot>, photoes: HashMap<String, Drawable>) {
        if (task != null) {
            for (document in task) {
                val curDate = document.id
                val year: Int = curDate.substring(0 until 4).toInt()
                val month: Int = curDate.substring(4 until 6).toInt()
                val date: Int = curDate.substring(6 until 8).toInt()
                Log.d(TAG, "${year} ${month}, ${date}")
                val calDay = CalendarDay.from(year, month, date)

                val curImage :Drawable = photoes.get(curDate)!!

                binding.calenMonthly.addDecorator(CurrentDayDecorator(requireActivity(), calDay, curImage))


            }
        }
    }
}