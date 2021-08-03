package com.example.woholi.ui.checklist.travel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woholi.databinding.FragmentTravelCategoryBinding
import com.example.woholi.model.CheckListItem
import com.example.woholi.model.CurrentUser
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class TravelCategoryFragment : Fragment() {

    private lateinit  var binding: FragmentTravelCategoryBinding
    val adapter: CheckListAdapter = CheckListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTravelCategoryBinding.inflate(inflater, container, false)
        adapter.checkList = mutableListOf()

        var documentName = arguments?.getString("title")
        adapter.title = documentName!!
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = readRoutine(documentName).await().documents
            val deferred2 = readRoutine2(documentName).await().documents
            initUI(deferred, deferred2)
        }

        return binding.root
    }

    suspend fun readRoutine(documentName: String?): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
            .collection("checklist").document("travel").collection(documentName!!)
            .whereEqualTo("isChecked", false)
            .get()
    }


    suspend fun readRoutine2(documentName: String?): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
            .collection("checklist").document("travel").collection(documentName!!)
            .whereEqualTo("isChecked", true)
            .get()
    }


    fun initUI(taskFalse: MutableList<DocumentSnapshot>, taskTrue: MutableList<DocumentSnapshot>){
        if (taskFalse != null) {
            for (document in taskFalse){
                adapter.checkList.add(CheckListItem("${document.id}", false))
            }
        }
        if (taskTrue != null) {
            for (document in taskTrue){
                adapter.checkList.add(CheckListItem("${document.id}", true))
            }
        }
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}