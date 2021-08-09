package com.example.woholi.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.woholi.databinding.ItemDiaryBinding
import com.example.woholi.model.Diary


class DiaryListAdapter: RecyclerView.Adapter<Holder>() {
    var diaryList = mutableListOf<Diary>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val diary = diaryList[position]
        holder.setCheck(diary)
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

}

class Holder(val binding: ItemDiaryBinding): RecyclerView.ViewHolder(binding.root){
    fun setCheck(diary: Diary){
        binding.txDate.text = diary.date
        binding.txTitle.text = diary.title
        Glide.with(binding.root).load(diary.url[0]).transform(CenterCrop(), RoundedCorners(50)).into(binding.imgUrl)
    }
}