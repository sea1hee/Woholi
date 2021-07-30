package com.example.woholi.ui.login


import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.woholi.ui.MainActivity
import com.example.woholi.R
import com.example.woholi.model.CurrentUser
import com.example.woholi.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
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


        binding.btnNext.setOnClickListener {
            updateDB()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnBack.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
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
        )

        Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("diary").document(today).set(tmpData)
        Firebase.firestore.collection("users").document(CurrentUser.uid)
            .collection("diary").document(today).collection("photo").add(hashMapOf("url" to "https://i.ytimg.com/vi/IT5Uq2K05C0/default.jpg"))

        val travelData1 = arrayOf("여권", "여권 사본", "항공권(E-티켓)", "영문 보험증서", "영문 이력서", "여권 사진", "한국 운전 면허증", "해외 결제 카드(VISA, Master)", "임시 숙소 주소")

        for (data in travelData1) {
            Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("travel").collection("document").document(data).set(hashMapOf("isChecked" to false))
        }

        val travelData2 = arrayOf("롱패딩", "코트", "자켓", "가죽자켓", "가디건", "잠옷", "원피스", "속옷", "양말", "수건", "모자", "맨투맨", "긴팔 티셔츠", "반팔 티셔츠", "청바지", "슬랙스")

        for (data in travelData2) {
            Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("travel").collection("clothes").document(data).set(hashMapOf("isChecked" to false))
        }

        val travelData3 = arrayOf("폼 클렌징", "클렌징 워터", "여행용 샴푸, 린스", "헤어 에센스", "로션", "스킨", "선크림", " 메이크업 제품", "화장솜", "면봉", "칫솔, 치약", "립스틱, 틴트", "아이브로우", "바디로션", "필링젤")

        for (data in travelData3) {
            Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("travel").collection("beauty").document(data).set(hashMapOf("isChecked" to false))
        }

        val travelData4 = arrayOf("데일밴드", "마데카솔", "피부과 약", "감기약", "소화제", "지사제", "생리통 약", "타이레놀", "영양제")

        for (data in travelData4) {
            Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("travel").collection("medicine").document(data).set(hashMapOf("isChecked" to false))
        }

        val travelData5 = arrayOf("노트북", "노트북 충전기", "카메라", "충전기", "보조배터리", "애플워치", "애플워치 충전기", "멀티탭", "110v 어댑터")

        for (data in travelData5) {
            Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("travel").collection("etc").document(data).set(hashMapOf("isChecked" to false))
        }

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
        Glide.with(this).load(CurrentUser.profile).circleCrop().into(binding.imageView)
    }


}