package com.example.woholi.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.woholi.ui.MainActivity
import com.example.woholi.R
import com.example.woholi.model.CurrentUser
import com.example.woholi.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
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
                Toast.makeText(this, "????????? ??????", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "????????? ??????")
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

            val docRef = db.collection("users").document(CurrentUser.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    Log.d(TAG, "?????? ????????? ?????? ?????? ??????")
                    if (document.exists()){
                        CurrentUser.email = document.data!!.get("email").toString()
                        CurrentUser.name = document.data!!.get("name").toString()
                        CurrentUser.profile = document.data!!.get("profile").toString()
                        CurrentUser.englishName = document.data!!.get("englishName").toString()
                        CurrentUser.birth = document.data!!.get("birth").toString()
                        CurrentUser.departure = document.data!!.get("departure").toString()
                        CurrentUser.location = document.data!!.get("location").toString()
                        loginSuccess()
                    }
                    else{
                        CurrentUser.email = curUser.email
                        CurrentUser.name = curUser.displayName
                        CurrentUser.profile = curUser.photoUrl.toString()
                        loginSuccessforNewUsers()
                    }
                }
                .addOnFailureListener{ exception ->
                    Log.d(TAG, "get failed with ", exception)
                }

        }


    }
    private fun loginSuccessforNewUsers(){
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginSuccess(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}