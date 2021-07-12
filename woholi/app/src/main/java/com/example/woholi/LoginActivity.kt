package com.example.woholi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.woholi.FindIdPassword.FindIdPasswordActivity
import com.example.woholi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            // 서버 추가
            // 입력한 아이디와 비밀번호가 맞는지 확인
            // 성공했다면
            // sharepre에 id, 닉네임 등 정 저장
            val intent_toMain = Intent(this, MainActivity::class.java)
            startActivity(intent_toMain)
            finish()
            // 아니라면
            // 토스트 띄우기
        }

        binding.txFindIdPassword.setOnClickListener {
            val intent_toFindIdPassword = Intent(this, FindIdPasswordActivity::class.java)
            startActivity(intent_toFindIdPassword)
        }

        binding.btnSingup.setOnClickListener {
            val intent_toSignUp = Intent(this, SignUpActivity::class.java)
            startActivity(intent_toSignUp)
            finish()
        }
    }
}