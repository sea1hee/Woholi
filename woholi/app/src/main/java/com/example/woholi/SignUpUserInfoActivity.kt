package com.example.woholi

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.example.woholi.Model.CurrentUser
import com.example.woholi.databinding.ActivitySignUpUserInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SignUpUserInfoActivity : AppCompatActivity() {

    var spnData = listOf("- Select -", "Victoria", "Toronto", "Vancouver", "Montreal" )
    val binding by lazy {ActivitySignUpUserInfoBinding.inflate(layoutInflater)}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var adapt = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spnData)
        binding.spnLocation.adapter = adapt

        binding.imageView.setImageURI(Uri.parse(CurrentUser.profile))


        binding.button.setOnClickListener {
            updateDB()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDB(){
        CurrentUser.englishName = binding.edtEnglishName.text.toString()
        CurrentUser.birth = binding.edtBirth.text.toString()
        CurrentUser.location = spnData[binding.spnLocation.selectedItemPosition]
        CurrentUser.departure = binding.edtDeparture.text.toString()

        Firebase.firestore.collection("users").document(CurrentUser.uid)
                .set(CurrentUser)

        Firebase.firestore.collection("users").document(CurrentUser.uid)
        // firestore에 해당 유저의 diary, checklist 추가

        val current = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val today = current.format(formatter)

        val tmpData = hashMapOf(
                "title" to "Hello Canada World!",
                "contents" to "I arrived!",
                "photo" to ""
        )

        Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("diary").document(today).set(tmpData)

        val tmpData2 = hashMapOf(
                "category" to "Have To do",
                "content" to "짐 정리하기",
                "ischecked" to false
        )

        Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").add(tmpData2)


    }
    private fun getUserInfo(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
        }
    }

}