package com.example.woholi.ui.checklist.shopping

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.model.CheckListItem
import com.example.woholi.model.CurrentUser
import com.example.woholi.databinding.RecyclerShoppinglistItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingItemAdapter: RecyclerView.Adapter<Holder2>() {
    var date: String = ""
    var checkList = mutableListOf<CheckListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder2 {
        val binding = RecyclerShoppinglistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder2(binding)
    }

    override fun onBindViewHolder(holder: Holder2, position: Int) {
        val check = checkList[position]
        holder.setCheck(check)

        holder.binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("shoppinglist").collection("first")
                .document(date).collection("second")
                .document(holder.binding.checkBox.text.toString())
                .update("isChecked", holder.binding.checkBox.isChecked)
                .addOnSuccessListener {
                    Log.d("CheckListAdapter2", "${holder.binding.checkBox.isChecked}")
                }
        }

    }

    override fun getItemCount(): Int {
        return checkList.size
    }

}

class Holder2(val binding: RecyclerShoppinglistItemBinding): RecyclerView.ViewHolder(binding.root){
    fun setCheck(checkListItem: CheckListItem){
        binding.checkBox.isChecked = checkListItem.isChecked
        binding.checkBox.text = checkListItem.content
    }
}