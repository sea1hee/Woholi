package com.example.woholi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.woholi.FindIdPassword.FindIdPasswordActivity
import com.example.woholi.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    var auth: FirebaseAuth? = null
    val GOOGLE_REQUEST_CODE = 99
    val TAG = "googleLogin"
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // Google Login
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        binding.btnGoogleLogin.setOnClickListener {
            signIn()
        }

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

    override fun onStart() {
        super.onStart()
        val curUser = FirebaseAuth.getInstance().currentUser
        if (curUser != null){
            loginSuccess()
            Log.d(TAG, "onStart")
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        //deprecated
        startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "로그인 성공")
                        val user = auth!!.currentUser
                        loginSuccessforNewUsers()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                    }
                }
    }

    private fun loginSuccessforNewUsers(){
        val intent = Intent(this, SignUpUserInfoActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginSuccess(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}