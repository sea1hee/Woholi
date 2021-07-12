package com.example.woholi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.woholi.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent_toLogin = Intent(this, LoginActivity::class.java)
            startActivity(intent_toLogin)
            finish()
        }

        val locationList = listOf("- Location -", "Toronto", "Vancouver", "Calgary")
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationList)
        binding.spnLocation.adapter = adapter


        binding.btnGoto.setOnClickListener {
            // 모든 항목이 입력되었는 지 확인

            // 디비 등록

            // 회원가입 완료 토스트


            val intent_toMain = Intent(this, MainActivity::class.java)
            startActivity(intent_toMain)
            finish()
        }

    }
}