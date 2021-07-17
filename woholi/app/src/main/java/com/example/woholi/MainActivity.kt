package com.example.woholi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.woholi.Navigation.*
import com.example.woholi.databinding.ActivityLoginBinding
import com.example.woholi.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val homeFragemnt by lazy { HomeFragment()}
    val checkListFragment by lazy { CheckListFragment() }
    val diaryFragment by lazy{ DiaryFragment() }
    val vsFragment by lazy { VsFragment() }
    val userInfoFragment by lazy { UserInfoFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, homeFragemnt).commit()

        binding.navigation.setOnItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId) {
            R.id.nvg_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, homeFragemnt)
                    .commit()
            }
            R.id.nvg_checkList -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, checkListFragment).commit()
            }
            R.id.nvg_diary -> {
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, diaryFragment)
                    .commit()
            }
            R.id.nvg_vs -> {
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, vsFragment)
                    .commit()
            }
            else -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, userInfoFragment).commit()
            }
        }
        true
    }

}