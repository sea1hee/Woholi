package com.example.woholi.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.woholi.R
import com.example.woholi.databinding.RecyclerPhotoBinding
import com.example.woholi.databinding.RecyclerPhotoHeaderBinding
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission

class AddPhotoAdapter() : RecyclerView.Adapter<AddPhotoAdapter.BaseViewHolder>() {
    var dataList = mutableListOf<String>()

    private val TYPE_HEADER: Int = 0
    private val TYPE_ITEM: Int = 1

    private lateinit var itemClickListener: ItemClickListener
    interface ItemClickListener{
        fun onClick(v:View, p :Int)
    }
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :BaseViewHolder{
        if (viewType == TYPE_ITEM){
            val itemBinding = RecyclerPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return mainHolder(itemBinding)
        }
        else{
            val headerBinding = RecyclerPhotoHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return headerHolder(headerBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == TYPE_HEADER) {
            return TYPE_HEADER
        } else {
            return TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is mainHolder){
            holder.setItem(dataList[position-1])
        }
        else if (holder is headerHolder){
            holder.binding.btnAddPhoto.setOnClickListener{
                itemClickListener.onClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList!!.size + 1
    }

    open class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    inner class headerHolder(val binding: RecyclerPhotoHeaderBinding):BaseViewHolder(binding.root){}

    class mainHolder(val binding: RecyclerPhotoBinding):BaseViewHolder(binding.root){
        fun setItem(data: String){
            Glide.with(itemView).load(data).into(itemView.findViewById(R.id.photoImage))
        }
    }


}
