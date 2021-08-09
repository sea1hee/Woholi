package com.example.woholi.ui.checklist.travel

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.model.CheckListItem
import com.example.woholi.model.CurrentUser
import com.example.woholi.databinding.RecyclerTravelItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TravelItemAdapter: RecyclerView.Adapter<Holder>() {
    var checkList = mutableListOf<CheckListItem>()
    var title: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerTravelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

class Holder(val binding: RecyclerTravelItemBinding): RecyclerView.ViewHolder(binding.root){
    fun setCheck(checkListItem: CheckListItem){
        binding.checkBox.isChecked = checkListItem.isChecked
        binding.checkBox.text = checkListItem.content
    }
}