package com.example.woholi.ui.diary

import android.app.Activity
import android.content.ContentResolver
import android.graphics.drawable.Drawable
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.io.InputStream
import java.net.URL

class CurrentDayDecorator(context: Activity?, currentDay: CalendarDay, photo: Drawable) : DayViewDecorator {

    private var myDay = currentDay
    private var curPhoto = photo
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(curPhoto)


    }

    init {
        // You can set background for Decorator via drawable here
    }
}