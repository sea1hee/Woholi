package com.example.woholi.ui.checklist.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woholi.databinding.FragmentShoppingListBinding
import com.example.woholi.model.Check
import com.example.woholi.model.CurrentUser
import com.example.woholi.model.ShoppingList
import com.example.woholi.navigation.checklist.shopping.ShoppingListAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShoppingListFragment : Fragment() {

    lateinit var binding: FragmentShoppingListBinding
    val adapter: ShoppingListAdapter = ShoppingListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false)

        init()
        return binding.root
    }


    fun init(){
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = readRoutine().await().documents
            if(deferred != null) {
                for ( document in deferred){
                    val newShoppingList = ShoppingList()
                    newShoppingList.date = document.id
                    val deffered2 = readRoutine2(document.id).await().documents
                    for (document2 in deffered2) {
                        newShoppingList.checks.add(
                            Check(
                                document2.id,
                                document2.data?.get("isChecked").toString().toBoolean()
                            )
                        )
                    }
                    addShoppingList(newShoppingList)
                }
            }
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    suspend fun readRoutine(): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
            .collection("checklist").document("shoppinglist").collection("first")
            .get()
    }

    suspend fun readRoutine2(documentName: String): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
            .collection("checklist").document("shoppinglist").collection("first")
            .document(documentName).collection("second").get()
    }

    fun addShoppingList(newList: ShoppingList){
        adapter.shoppingList.add(newList)
    }

}