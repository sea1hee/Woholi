package com.example.woholi.Navigation.checklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.Model.Check
import com.example.woholi.databinding.RecyclerTCheckBinding

class CheckListAdapter: RecyclerView.Adapter<Holder>() {
    var checkList = mutableListOf<Check>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerTCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val check = checkList[position]
        holder.setCheck(check)
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