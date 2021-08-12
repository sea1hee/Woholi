package com.example.woholi.ui.diary

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.woholi.R
import com.example.woholi.databinding.FragmentPhotoBinding
import com.google.firebase.storage.FirebaseStorage

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
        FirebaseStorage.getInstance().reference.child(photoUrl!!).downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(binding.imgPhoto)
        }.addOnFailureListener {
            // Handle any errors
        }

        return binding.root
    }
}