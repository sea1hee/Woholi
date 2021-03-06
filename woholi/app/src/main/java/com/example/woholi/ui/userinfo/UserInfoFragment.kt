package com.example.woholi.ui.userinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.ui.MainActivity
import com.example.woholi.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {

    private lateinit var binding : FragmentUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            (activity as MainActivity).logOut()
        }
        binding.btnDeleteLog.setOnClickListener {
            (activity as MainActivity).deleteLog()
        }
    }
}