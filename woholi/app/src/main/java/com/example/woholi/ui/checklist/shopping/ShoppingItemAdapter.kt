package com.example.woholi.ui.checklist.shopping

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.adapter.AddPhotoAdapter
import com.example.woholi.model.CheckListItem
import com.example.woholi.model.CurrentUser
import com.example.woholi.databinding.RecyclerShoppinglistItemBinding
import com.example.woholi.databinding.RecyclerShoppinglistItemFooterBinding
import com.example.woholi.databinding.RecyclerShoppinglistItemHeaderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingItemAdapter: RecyclerView.Adapter<ShoppingItemAdapter.BaseViewHolder>() {
    var title: String = ""
    var checkList = mutableListOf<CheckListItem>()

    private val TYPE_HEADER: Int = 0
    private val TYPE_ITEM: Int = 1
    private val TYPE_FOOTER: Int = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding = RecyclerShoppinglistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemHolder(binding)
        }
        else if (viewType == TYPE_HEADER){
            val binding = RecyclerShoppinglistItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HeaderHolder(binding)
        }
        else{
            val binding = RecyclerShoppinglistItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FooterHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == TYPE_HEADER){
            return TYPE_HEADER
        }
        else if (position == TYPE_ITEM){
            return TYPE_ITEM
        }
        else{
            return TYPE_FOOTER
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.setCheck(checkList[position - 1])

            holder.binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                Firebase.firestore.collection("users").document(CurrentUser.uid)
                        .collection("checklist").document("shoppinglist").collection("first")
                        .document(title).collection("second")
                        .document(holder.binding.checkBox.text.toString())
                        .update("isChecked", holder.binding.checkBox.isChecked)
                        .addOnSuccessListener {
                            Log.d("CheckListAdapter2", "${holder.binding.checkBox.isChecked}")
                        }
            }
        }
    }

    override fun getItemCount(): Int {
        return checkList.size + 2
    }

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class HeaderHolder(val binding: RecyclerShoppinglistItemHeaderBinding): BaseViewHolder(binding.root){
    }

    class ItemHolder(val binding: RecyclerShoppinglistItemBinding): BaseViewHolder(binding.root){
        fun setCheck(checkListItem: CheckListItem){
            binding.checkBox.isChecked = checkListItem.isChecked
            binding.checkBox.text = checkListItem.content
        }
    }

    class FooterHolder(val binding: RecyclerShoppinglistItemFooterBinding): BaseViewHolder(binding.root){}



}