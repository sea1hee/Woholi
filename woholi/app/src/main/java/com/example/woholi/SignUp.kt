package com.example.woholi


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.woholi.Model.CurrentUser
import com.example.woholi.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDate.*
import java.time.format.DateTimeFormatter
import java.util.*

class SignUp : AppCompatActivity() {

    var spnData = listOf("- Select -", "Victoria", "Toronto", "Vancouver", "Montreal" )
    val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater)}
    var isReturnValue: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH) + 1
        var day = c.get(Calendar.DAY_OF_MONTH)
        binding.btnBirth.setText("${year} - ${month} - ${day}")
        binding.btnDeparture.setText("${year} - ${month} - ${day}")


        binding.btnBirth.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.datepicker, null)
            val dialogId: DatePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    c.set(Calendar.YEAR, dialogId.year)
                    c.set(Calendar.MONTH, dialogId.month)
                    c.set(Calendar.DAY_OF_MONTH, dialogId.dayOfMonth)
                    val dataFormat = SimpleDateFormat("yyyyMMdd")
                    CurrentUser.birth = "${dataFormat.format(c.time)}"
                    binding.btnBirth.setText("${dialogId.year} - ${dialogId.month+1} - ${dialogId.dayOfMonth}")
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    isReturnValue = false
                }
                .show()
        }

        binding.btnDeparture.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.datepicker, null)
            val dialogId: DatePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    c.set(Calendar.YEAR, dialogId.year)
                    c.set(Calendar.MONTH, dialogId.month)
                    c.set(Calendar.DAY_OF_MONTH, dialogId.dayOfMonth)
                    val dataFormat = SimpleDateFormat("yyyyMMdd")
                    CurrentUser.departure = "${dataFormat.format(c.time)}"
                    binding.btnDeparture.setText("${dialogId.year} - ${dialogId.month+1} - ${dialogId.dayOfMonth}")
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    isReturnValue = false
                }
                .show()
        }




        var adapt = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spnData)
        binding.spnLocation.adapter = adapt


        setProfile()


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
        CurrentUser.location = spnData[binding.spnLocation.selectedItemPosition]

        Firebase.firestore.collection("users").document(CurrentUser.uid)
                .set(CurrentUser)

        Firebase.firestore.collection("users").document(CurrentUser.uid)

        val current = now()
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

    fun openDialogDatePicker(): DatePicker{
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.datepicker, null)
        val dialogId: DatePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)

        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                CurrentUser.birth = "${dialogId.year}${dialogId.month+1}${dialogId.dayOfMonth}"
                binding.btnBirth.setText("${dialogId.year} - ${dialogId.month+1} - ${dialogId.dayOfMonth}")
            }
            .setNegativeButton("취소") { dialogInterface, i ->
                isReturnValue = false
            }
            .show()
        return dialogId
    }


    fun setProfile(){
        Glide.with(this).load(CurrentUser.profile).into(binding.imageView)
    }


}