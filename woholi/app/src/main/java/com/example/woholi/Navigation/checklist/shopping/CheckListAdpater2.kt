package com.example.woholi.Navigation.checklist.shopping

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.MainActivity
import com.example.woholi.Model.Check
import com.example.woholi.Model.CurrentUser
import com.example.woholi.databinding.RecyclerSCheckBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CheckListAdapter2: RecyclerView.Adapter<Holder2>() {
    var date: String = ""
    var checkList = mutableListOf<Check>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder2 {
        val binding = RecyclerSCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

class Holder2(val binding: RecyclerSCheckBinding): RecyclerView.ViewHolder(binding.root){
    fun setCheck(check: Check){
        binding.checkBox.isChecked = check.isChecked
        binding.checkBox.text = check.content
    }
}