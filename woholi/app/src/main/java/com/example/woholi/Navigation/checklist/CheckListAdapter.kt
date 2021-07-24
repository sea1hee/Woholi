package com.example.woholi.Navigation.checklist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.Model.Check
import com.example.woholi.Model.CurrentUser
import com.example.woholi.databinding.RecyclerTCheckBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CheckListAdapter: RecyclerView.Adapter<Holder>() {
    var checkList = mutableListOf<Check>()
    var title: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerTCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val check = checkList[position]
        holder.setCheck(check)

        holder.binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            Firebase.firestore.collection("users").document(CurrentUser.uid)
                    .collection("checklist").document("travel").collection(title)
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

class Holder(val binding: RecyclerTCheckBinding): RecyclerView.ViewHolder(binding.root){
    fun setCheck(check: Check){
        binding.checkBox.isChecked = check.isChecked
        binding.checkBox.text = check.content
    }
}