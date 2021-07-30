package com.example.woholi.ui.diary

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.woholi.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPhotoBinding.inflate(inflater, container, false)

        var photoUrl = arguments?.getString("url")
        Glide.with(this)
            .load(photoUrl)
            .into(binding.imgPhoto)

        return binding.root
    }
}