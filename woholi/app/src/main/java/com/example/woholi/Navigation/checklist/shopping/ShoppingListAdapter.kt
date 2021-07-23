package com.example.woholi.Navigation.checklist.shopping

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.Model.Check
import com.example.woholi.Model.CurrentUser
import com.example.woholi.Model.ShoppingList
import com.example.woholi.databinding.RecyclerShoppingDailyBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingListAdapter:RecyclerView.Adapter<Holder>() {
    var shoppingList = mutableListOf<ShoppingList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerShoppingDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

class Holder(val binding: RecyclerShoppingDailyBinding): RecyclerView.ViewHolder(binding.root) {
    fun setShoppingList(shoppinglist: ShoppingList, position: Int) {
        binding.txDate.text = shoppinglist.date

        binding.layoutEnter.visibility = View.INVISIBLE
        val adapter: CheckListAdapter2 = CheckListAdapter2()
        adapter.date = shoppinglist.date
        adapter.checkList.addAll(shoppinglist.checks)
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = LinearLayoutManager(itemView.context)

        binding.btnAdd.setOnClickListener {
            binding.layoutEnter.visibility = View.VISIBLE

            binding.btnAddNew.setOnClickListener {
                if (binding.editTextTextPersonName.text.toString() != "") {
                    adapter.checkList.add(
                        Check(
                            binding.editTextTextPersonName.text.toString(),
                            false
                        )
                    )
                    adapter.notifyDataSetChanged()

                    Firebase.firestore.collection("users").document(CurrentUser.uid)
                        .collection("checklist").document("shoppinglist").collection("first")
                        .document(shoppinglist.date).collection("second")
                        .document(binding.editTextTextPersonName.text.toString())
                        .set(hashMapOf("isChecked" to false))

                }

                binding.layoutEnter.visibility = View.INVISIBLE

            }
        }

    }
}


