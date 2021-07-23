package com.example.woholi.Navigation.checklist.shopping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.Model.Check
import com.example.woholi.Model.ShoppingList
import com.example.woholi.Navigation.checklist.CheckListAdapter
import com.example.woholi.databinding.RecyclerShoppingDailyBinding

class ShoppingListAdapter:RecyclerView.Adapter<Holder>() {
    var shoppingList = mutableListOf<ShoppingList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerShoppingDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val shoppinglist = shoppingList[position]
        holder.setShoppingList(shoppinglist)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }
}

class Holder(val binding: RecyclerShoppingDailyBinding): RecyclerView.ViewHolder(binding.root){
    fun setShoppingList(shoppinglist: ShoppingList){
        binding.txDate.text = shoppinglist.date

        val adapter: CheckListAdapter = CheckListAdapter()
        adapter.checkList = mutableListOf()
        adapter.checkList.add(Check("example1", true))
        adapter.checkList.add(Check("example2", false))
        adapter.checkList.add(Check("example3", true))
        adapter.checkList.addAll(shoppinglist.checks)
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = LinearLayoutManager(itemView.context)
    }
}

