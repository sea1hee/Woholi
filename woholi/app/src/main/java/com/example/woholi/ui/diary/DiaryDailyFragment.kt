package com.example.woholi.ui.diary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.adapter.ViewPagerAdapter
import com.example.woholi.databinding.FragmentDiaryDailyBinding
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DiaryDailyFragment : Fragment() {

    private lateinit var binding : FragmentDiaryDailyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val curDay = arguments?.getString("curDay")
        binding.btnBack.text = curDay

        CoroutineScope(Dispatchers.Main).launch {
            val deferred = readPhotoes(curDay!!).await().documents
            initUI(deferred)
        }

        binding.btnBack.setOnClickListener {
            (activity as MainActivity).setFlag(7)
        }

        binding.btnEdit.setOnClickListener {
            (activity as MainActivity).setFlag(5)
        }
    }

    suspend fun readPhotoes(curDay: String): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
            .collection("diary").document(curDay).collection("photo").get()
    }

    fun initUI(task: MutableList<DocumentSnapshot>) {
        if (task != null) {
            val adapter = ViewPagerAdapter(requireActivity())
            for (document in task) {
                Log.d("Diary", "I find")
                val newFragment = PhotoFragment()
                val bundle = Bundle()
                bundle.putString("url", document.data?.get("url").toString())
                Log.d("Diary", "I find")
                newFragment.arguments = bundle
                adapter.addFragment(newFragment)
            }
            binding.viewPagerDaily.adapter = adapter
            binding.dotsIndicator.setViewPager2(binding.viewPagerDaily)
        }
    }
}