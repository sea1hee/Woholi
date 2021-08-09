package com.example.woholi.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.woholi.databinding.FragmentHomeBinding
import com.example.woholi.db.DiaryViewModel
import com.example.woholi.db.ShoppingListViewModel
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.checklist.shopping.ShoppingCategoryAdapter
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    val diaryVM by viewModels<DiaryViewModel>({requireActivity()})

    val adapter_diary: DiaryListAdapter = DiaryListAdapter()
    val adapter_shopping : ShoppingCategoryAdapter = ShoppingCategoryAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(CurrentUser.profile).circleCrop().into(binding.imageViewProfile)
        binding.txNickname.text = CurrentUser.name

        diaryVM.diaryList.observe(viewLifecycleOwner) {
            adapter_diary.diaryList = diaryVM.DiaryList
            if (binding.tabLayout2.selectedTabPosition == 0) {
                binding.recyclerViewHome.adapter = adapter_diary
                binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext()).also {it.orientation = LinearLayoutManager.HORIZONTAL}
            }

        }


        binding.tabLayout2.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 ->{
                        binding.recyclerViewHome.adapter = adapter_diary
                        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext()).also {it.orientation = LinearLayoutManager.HORIZONTAL}

                    }
                    1 ->{
                        binding.recyclerViewHome.adapter = adapter_shopping
                        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

        })
    }

}