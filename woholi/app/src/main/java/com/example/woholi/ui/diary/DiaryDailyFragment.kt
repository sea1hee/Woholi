package com.example.woholi.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.woholi.R
import com.example.woholi.adapter.ViewPagerAdapter
import com.example.woholi.databinding.FragmentDiaryDailyBinding
import com.example.woholi.db.DiaryViewModel
import com.example.woholi.ui.MainActivity
import androidx.fragment.app.viewModels

class DiaryDailyFragment : Fragment() {

    private lateinit var binding : FragmentDiaryDailyBinding

    val diaryVM by viewModels<DiaryViewModel>({requireActivity()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary_daily, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val curDay = arguments?.getString("curDay")
        binding.btnBack.text = curDay

        val curDiary = diaryVM.getDiary(curDay!!)


        val adapter = ViewPagerAdapter(requireActivity())
        for (i in 0..curDiary!!.url.size-1) {
            val newFragment = PhotoFragment()
            val bundle = Bundle()
            bundle.putString("url", curDiary!!.url[i])
            newFragment.arguments = bundle
            adapter.addFragment(newFragment)
        }
        binding.viewPagerDaily.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPagerDaily)

        binding.btnBack.setOnClickListener {
            (activity as MainActivity).setFlag(7)
        }

        binding.btnEdit.setOnClickListener {
            (activity as MainActivity).setFlag(5)
        }
    }

}