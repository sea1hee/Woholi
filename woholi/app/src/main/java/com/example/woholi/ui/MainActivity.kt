package com.example.woholi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable
import com.example.woholi.R
import com.example.woholi.databinding.ActivityMainBinding
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.checklist.CheckListFragment
import com.example.woholi.ui.diary.DiaryDailyFragment
import com.example.woholi.ui.diary.DiaryFragment
import com.example.woholi.ui.diary.WriteDiaryFragment
import com.example.woholi.ui.home.HomeFragment
import com.example.woholi.ui.login.LoginActivity
import com.example.woholi.ui.userinfo.UserInfoFragment
import com.example.woholi.ui.vs.VsFragment
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(){

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val homeFragemnt by lazy { HomeFragment() }
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
            0 -> supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, homeFragemnt).commit()
            1 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, checkListFragment).commit()
            2 -> supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, diaryFragment).commit()
            3 -> supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, vsFragment).commit()
            4 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, userInfoFragment).commit()
            7 -> supportFragmentManager.popBackStack()
        }
    }

    fun setFlag(flag: Int, curDay:String){
        val bundle: Bundle = Bundle()
        bundle.putString("curDay", curDay)
        when(flag){
            5 -> {
                val newFragment = WriteDiaryFragment()
                newFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .add(R.id.frameLayout, newFragment, curDay).addToBackStack(null).commit()
            }
            6 -> {
                val newFragment = DiaryDailyFragment()
                newFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .add(R.id.frameLayout, newFragment, curDay).addToBackStack(null).commit()
            }
            7 -> supportFragmentManager.popBackStack()
        }

    }


    fun logOut(){
        val auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(this,gso)


        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        val mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, GoogleApiClient.OnConnectionFailedListener {  })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        mGoogleApiClient!!.connect()
        mGoogleApiClient!!.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(@Nullable bundle: Bundle?) {
                auth!!.signOut()
                if (mGoogleApiClient!!.isConnected) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(object : ResultCallback<Status?> {
                        override fun onResult(status: Status) {
                            if (status.isSuccess()) {
                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                            }
                            finish()
                        }
                    })
                }
            }

            override fun onConnectionSuspended(i: Int) {
                finish()
            }
        })
    }

    fun deleteLog(){
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }

        Firebase.firestore.collection("users").document(CurrentUser.uid)
            .delete()
        logOut()
    }

}