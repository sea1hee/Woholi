package com.example.woholi.Navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.woholi.LoginActivity
import com.example.woholi.MainActivity
import com.example.woholi.R
import com.example.woholi.databinding.FragmentUserInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo

class UserInfoFragment : Fragment() {

    private var binding : FragmentUserInfoBinding? = null
    private lateinit var logOut: Button
    private lateinit var deleteLog: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserInfoFragment().apply {
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logOut = view.findViewById(R.id.btn_logout)
        deleteLog = view.findViewById((R.id.btn_delete_log))

        logOut.setOnClickListener {
            (activity as MainActivity).logOut()
        }
        deleteLog.setOnClickListener {
            (activity as MainActivity).deleteLog()
        }
    }
}