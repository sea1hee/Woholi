package com.example.woholi.ui.checklist.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.databinding.RecyclerShoppinglistCategoryBinding
import com.example.woholi.model.CheckListItem
import com.example.woholi.model.CurrentUser
import com.example.woholi.model.CheckListCategory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingCategoryAdapter:RecyclerView.Adapter<Holder>() {
    var shoppingList = mutableListOf<CheckListCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerShoppinglistCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val shoppinglist = shoppingList[position]
        holder.setShoppingList(shoppinglist, position)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

}

class Holder(val binding: RecyclerShoppinglistCategoryBinding): RecyclerView.ViewHolder(binding.root) {
    fun setShoppingList(shoppinglist: CheckListCategory, position: Int) {

        val adapter: ShoppingItemAdapter = ShoppingItemAdapter()
        adapter.title = shoppinglist.title
        adapter.checkList.addAll(shoppinglist.checkListItems)
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = LinearLayoutManager(itemView.context)
    }
}


