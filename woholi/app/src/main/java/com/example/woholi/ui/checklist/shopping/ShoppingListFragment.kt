package com.example.woholi.ui.checklist.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woholi.databinding.FragmentShoppingListBinding
import com.example.woholi.db.DiaryViewModel
import com.example.woholi.db.ShoppingListViewModel
import com.example.woholi.model.CheckListItem
import com.example.woholi.model.CurrentUser
import com.example.woholi.model.CheckListCategory
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

    val shoppingVm by viewModels<ShoppingListViewModel>({requireActivity()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingVm.shoppingList.observe(viewLifecycleOwner){
            adapter.shoppingList = shoppingVm.ShoppingList
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}