package com.example.woholi.ui.checklist.shopping

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.example.woholi.R
import com.example.woholi.adapter.AddPhotoAdapter
import com.example.woholi.model.CheckListItem
import com.example.woholi.model.CurrentUser
import com.example.woholi.databinding.RecyclerShoppinglistItemBinding
import com.example.woholi.databinding.RecyclerShoppinglistItemFooterBinding
import com.example.woholi.databinding.RecyclerShoppinglistItemHeaderBinding
import com.example.woholi.db.ShoppingListRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingItemAdapter: RecyclerView.Adapter<ShoppingItemAdapter.BaseViewHolder>() {
    var title: String = ""
    var checkList = mutableListOf<CheckListItem>()
    val repository = ShoppingListRepository()


    private val TYPE_HEADER: Int = 0
    private val TYPE_ITEM: Int = 1
    private val TYPE_FOOTER: Int = 2

    lateinit var headerBinding :RecyclerShoppinglistItemHeaderBinding
    lateinit var footerBinding :RecyclerShoppinglistItemFooterBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == TYPE_ITEM) {
            val itemBinding = RecyclerShoppinglistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemHolder(itemBinding)
        }
        else if (viewType == TYPE_HEADER){
            headerBinding = RecyclerShoppinglistItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HeaderHolder(headerBinding)
        }
        else{
            footerBinding = RecyclerShoppinglistItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FooterHolder(footerBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0){
            return TYPE_HEADER
        }
        else if (position == checkList.size + 1){
            return TYPE_FOOTER
        }
        else{
            return TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.setCheck(checkList[position - 1], title)
            holder.binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                repository.updateCheckBox(title, holder.binding.checkBox.text.toString(), holder.binding.checkBox.isChecked)
            }
        }
        else if (holder is HeaderHolder){
            holder.setHeader(title)
            holder.binding.btnAdd.setOnClickListener {
                if (footerBinding.recyclerFooter.visibility == View.VISIBLE) {
                    footerBinding.recyclerFooter.visibility = View.INVISIBLE

                }
                else{
                    footerBinding.recyclerFooter.visibility = View.VISIBLE
                }
            }
        }
        else if (holder is FooterHolder){
            holder.setFooter()

            holder.binding.button.setOnClickListener{
                holder.binding.recyclerFooter.visibility = View.VISIBLE
                val contents = holder.binding.edtContents.text.toString()
                if (contents != "") {
                    repository.writeNewContents(title, contents)
                    checkList.add(CheckListItem(contents, false))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return checkList.size + 2
    }

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class HeaderHolder(val binding: RecyclerShoppinglistItemHeaderBinding): BaseViewHolder(binding.root){
        fun setHeader(title: String){
            binding.txDate.text = title
        }
    }

    class ItemHolder(val binding: RecyclerShoppinglistItemBinding): BaseViewHolder(binding.root){
        fun setCheck(checkListItem: CheckListItem, title: String){
            binding.checkBox.isChecked = checkListItem.isChecked
            binding.checkBox.text = checkListItem.content
        }
    }

    class FooterHolder(val binding: RecyclerShoppinglistItemFooterBinding): BaseViewHolder(binding.root) {
        fun setFooter(){
            binding.recyclerFooter.visibility = View.INVISIBLE
        }

    }


}