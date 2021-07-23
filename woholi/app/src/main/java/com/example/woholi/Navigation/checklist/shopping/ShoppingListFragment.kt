package com.example.woholi.Navigation.checklist.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woholi.Model.Check
import com.example.woholi.Model.CurrentUser
import com.example.woholi.Model.ShoppingList
import com.example.woholi.databinding.FragmentShoppingListBinding
import com.google.android.gms.tasks.Task
import com.google.api.Distribution
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShoppingListFragment : Fragment() {

    private var binding: FragmentShoppingListBinding? = null
    val adapter: ShoppingListAdapter = ShoppingListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false)

        adapter.shoppingList = mutableListOf()
        adapter.shoppingList.add(ShoppingList("2021-07-24"))
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //CoroutineScope(Dispatchers.Main).launch {
         //   val deferred = readRoutine().await().documents
          //  initUI(deferred)
        //}

        return binding!!.root
    }
/*
    suspend fun readRoutine(): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("travel").collection()
                .whereEqualTo("isChecked", false)
                .get()
    }



    fun initUI(taskFalse: MutableList<DocumentSnapshot>){
        if (taskFalse != null) {
            for (document in taskFalse){
                adapter.checkList.add(Check("${document.id}", false))
            }
        }
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    */

}