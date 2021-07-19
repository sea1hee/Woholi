package com.example.woholi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.woholi.Model.CurrentUser
import com.example.woholi.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    var auth: FirebaseAuth? = null
    val GOOGLE_REQUEST_CODE = 99
    val TAG = "googleLogin"
    private lateinit var googleSignInClient: GoogleSignInClient

    val db = Firebase.firestore


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

    }

    override fun onStart() {
        super.onStart()
        val curUser = FirebaseAuth.getInstance().currentUser
        if (curUser != null){
            checkIsNewUser(curUser)
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
                        checkIsNewUser(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                    }
                }
    }

    private fun checkIsNewUser(curUser: FirebaseUser?) {

        curUser?.let {
            CurrentUser.uid = curUser.uid
            CurrentUser.email = curUser.email
            CurrentUser.name = curUser.displayName
            CurrentUser.profile = curUser.photoUrl?.toString()

            val docRef = db.collection("users").document(CurrentUser.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    Log.d(TAG, "유저 로그인 기록 조회 성공")
                    if (document.exists()){
                        CurrentUser.englishName = document.data!!.get("englishName").toString()
                        CurrentUser.birth = document.data!!.get("birth").toString()
                        CurrentUser.departure = document.data!!.get("departure").toString()
                        CurrentUser.location = document.data!!.get("location").toString()
                        loginSuccess()
                    }
                    else{
                        loginSuccessforNewUsers()
                    }
                }
                .addOnFailureListener{ exception ->
                    Log.d(TAG, "get failed with ", exception)
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