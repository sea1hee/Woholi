package com.example.woholi.db

import android.util.Log
import com.example.woholi.model.CurrentUser
import com.example.woholi.model.Diary
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DiaryRepository {

    val TAG = "Repository"
    val db = Firebase.firestore

    suspend fun getDiary(): List<Diary>{
        val cour = CoroutineScope(Dispatchers.IO).async {
            val deferred = readDiary().await().documents
            val diaryList :MutableList<Diary> = mutableListOf()
            for (document in deferred) {
                if (document != null) {
                    val newDiary: Diary? = document.toObject(Diary::class.java)
                    if (newDiary != null) {
                        diaryList.add(0, newDiary)
                        Log.d(TAG, "${newDiary.url}")
                    }
                }
            }
            diaryList
        }
        return cour.await()
    }

    suspend fun readDiary(): Task<QuerySnapshot> {
        return db.collection("users").document(CurrentUser.uid)
                .collection("diary").get()
    }


    fun writeDiary(diary: Diary){
        db.collection("users").document(CurrentUser.uid)
                .collection("diary").document(diary.date)
                .set(diary)
    }
}