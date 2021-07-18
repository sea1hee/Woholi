package com.example.woholi.Navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.MainActivity
import com.example.woholi.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {

    private var binding : FragmentUserInfoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?ㅗ ㅈ
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

        binding!!.btnLogout.setOnClickListener {
            (activity as MainActivity).logOut()
        }
        binding!!.btnDeleteLog.setOnClickListener {
            (activity as MainActivity).deleteLog()
        }
    }
}