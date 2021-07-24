package com.example.woholi.Navigation.checklist

import android.graphics.DiscretePathEffect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woholi.Model.Check
import com.example.woholi.Model.CurrentUser
import com.example.woholi.R
import com.example.woholi.databinding.FragmentTCheckListBinding
import com.example.woholi.databinding.FragmentTravelItemBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class TCheckListFragment : Fragment() {

    private var binding: FragmentTCheckListBinding? = null
    val adapter: CheckListAdapter = CheckListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTCheckListBinding.inflate(inflater, container, false)
        adapter.checkList = mutableListOf()

        var documentName = arguments?.getString("title")
        adapter.title = documentName!!
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = readRoutine(documentName).await().documents
            val deferred2 = readRoutine2(documentName).await().documents
            initUI(deferred, deferred2)
        }

        return binding!!.root
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
                adapter.checkList.add(Check("${document.id}", false))
            }
        }
        if (taskTrue != null) {
            for (document in taskTrue){
                adapter.checkList.add(Check("${document.id}", true))
            }
        }
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}