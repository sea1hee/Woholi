package com.example.woholi.FindIdPassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.woholi.databinding.ActivityFindIdPasswordBinding
import com.google.android.material.tabs.TabLayoutMediator

class FindIdPasswordActivity : AppCompatActivity() {

    val binding by lazy { ActivityFindIdPasswordBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragmentList = listOf(FindIdFragment(), FindPasswordFragment())

        val adapter = FindIdPasswordFragmentAdapter(this)
        adapter.fragmentList = fragmentList
        binding.viewPager.adapter = adapter

        val tabTitles = listOf<String>("아이디 찾기", "비밀번호 찾기")
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        binding.btnBack.setOnClickListener {
            finish()
        }



    }
}