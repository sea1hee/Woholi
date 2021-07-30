package com.example.woholi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.woholi.R

class AddPhotoAdapter() : RecyclerView.Adapter<AddPhotoAdapter.BaseViewHolder>() {
    var dataList = mutableListOf<String>()

    private val TYPE_HEADER: Int = 0
    private val TYPE_ITEM: Int = 1

    private var onItemClick: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :BaseViewHolder{
        if (viewType == TYPE_ITEM){
            val mainView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.recycler_photo, parent, false)
            return mainHolder(mainView)
        }
        else{
            val headView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.recycler_photo_header, parent, false)
            return headerHolder(headView)
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
    }

    override fun getItemCount(): Int {
        return dataList!!.size + 1
    }

    open class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    class headerHolder(itemView: View):BaseViewHolder(itemView){}

    class mainHolder(itemView: View):BaseViewHolder(itemView){
        fun setItem(data: String){
            Glide.with(itemView).load(data).into(itemView.findViewById(R.id.photoImage))
        }
    }


}
