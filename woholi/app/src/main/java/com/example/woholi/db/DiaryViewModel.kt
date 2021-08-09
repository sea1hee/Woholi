package com.example.woholi.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woholi.model.Diary
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch

class DiaryViewModel : ViewModel() {

    val DiaryList = mutableListOf<Diary>()

    private val _diaryList = MutableLiveData<List<Diary>>()
    var diaryList: LiveData<List<Diary>> = _diaryList

    val repository = DiaryRepository()

    init{
        viewModelScope.launch{
            DiaryList.addAll(repository.getDiary().toMutableList())
            _diaryList.value = DiaryList
        }
    }


    fun getDiary(curDate: String) : Diary? {
        for (i in 0..DiaryList.size-1) {
            if (DiaryList[i].date == curDate) {
                return DiaryList[i]
            }
        }
        return null
    }

    fun existDate(date:CalendarDay):Boolean {
        val curDay = changeFormatDate(date)

        if (getDiary(curDay) != null) return true

        return false
    }


    fun changeFormatDate(date: CalendarDay):String{

        val curMonth = if (date.month < 10) "0${date.month}" else date.month.toString()
        val curDate = if (date.day < 10) "0${date.day}" else date.day.toString()

        return "${date.year}${curMonth}${curDate}"
    }
}