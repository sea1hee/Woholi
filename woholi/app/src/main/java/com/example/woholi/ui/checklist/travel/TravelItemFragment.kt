package com.example.woholi.ui.checklist.travel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.adapter.ViewPagerAdapter
import com.example.woholi.databinding.FragmentTravelItemBinding
import com.google.android.material.tabs.TabLayoutMediator

class TravelItemFragment : Fragment() {

    private lateinit var binding: FragmentTravelItemBinding

    private val documents by lazy { TravelCategoryFragment() }
    private val clothes by lazy {TravelCategoryFragment() }
    private val beauty by lazy { TravelCategoryFragment() }
    private val medicine by lazy { TravelCategoryFragment() }
    private val etc by lazy { TravelCategoryFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTravelItemBinding.inflate(inflater, container, false)


        var bundle = Bundle()
        bundle.putString("title", "document")
        documents.arguments = bundle

        bundle = Bundle()
        bundle.putString("title", "clothes")
        clothes.arguments = bundle

        bundle = Bundle()
        bundle.putString("title", "beauty")
        beauty.arguments = bundle

        bundle = Bundle()
        bundle.putString("title", "medicine")
        medicine.arguments = bundle

        bundle = Bundle()
        bundle.putString("title", "etc")
        etc.arguments = bundle

        val fragmentList = mutableListOf<Fragment>(documents, clothes, beauty, medicine, etc)
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.fragmentList = fragmentList
        binding.viewPagerTravel.adapter = adapter

        val tabTitle = listOf<String>("서류", "의류", "화장품", "약", "기타")
        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPagerTravel){tab, position ->
            tab.text = tabTitle[position]
        }.attach()


        return binding.root
    }

}