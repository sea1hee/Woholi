package com.example.woholi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.woholi.Model.CurrentUser
import com.example.woholi.Navigation.*
import com.example.woholi.databinding.ActivityLoginBinding
import com.example.woholi.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(){

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val homeFragemnt by lazy { HomeFragment()}
    val checkListFragment by lazy { CheckListFragment() }
    val diaryFragment by lazy{ DiaryFragment() }
    val vsFragment by lazy { VsFragment() }
    val userInfoFragment by lazy { UserInfoFragment() }

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFlag(0)
        binding.navigation.setOnItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId) {
            R.id.nvg_home -> setFlag(0)
            R.id.nvg_checkList -> setFlag(1)
            R.id.nvg_diary -> setFlag(2)
            R.id.nvg_vs -> setFlag(3)
            else -> setFlag(4)
        }
        true
    }

    fun setFlag(flag: Int){
        when(flag) {
            0 -> supportFragmentManager.beginTransaction().replace(R.id.frameLayout, homeFragemnt).commit()
            1 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, checkListFragment).commit()
            2 -> supportFragmentManager.beginTransaction().replace(R.id.frameLayout, diaryFragment)
                    .commit()
            3 -> supportFragmentManager.beginTransaction().replace(R.id.frameLayout, vsFragment)
                    .commit()
            4 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, userInfoFragment).commit()
        }
    }

    fun logOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun deleteLog(){
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
ㅕㅑ
        Firebase.firestore.collection("users").document(CurrentUser.uid)
            .delete()
        logOut()
    }
}