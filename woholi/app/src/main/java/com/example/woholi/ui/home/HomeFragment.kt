package com.example.woholi.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.woholi.BuildConfig
import com.example.woholi.databinding.FragmentHomeBinding
import com.example.woholi.db.*
import com.example.woholi.db.exchange.ExchangeRate
import com.example.woholi.db.weather.Weather
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.checklist.shopping.ShoppingCategoryAdapter
import com.google.android.material.tabs.TabLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    val diaryVM by viewModels<DiaryViewModel>({requireActivity()})
    val shoppingVM by viewModels<ShoppingListViewModel>({requireActivity()})

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

        binding.txCity.text = CurrentUser.location
        CoroutineScope(Dispatchers.Main).launch {
            binding.txDegree.text = getDegree()
            binding.txExchangerate.text = getExchangeRateCanada()
        }
        binding.txDday.text = "D + ${setDday()}"
        binding.progressbar.progress = setDday()* 100 / 365



        diaryVM.diaryList.observe(viewLifecycleOwner) {
            adapter_diary.diaryList = diaryVM.DiaryList
            if (binding.tabLayout2.selectedTabPosition == 0) {
                binding.recyclerViewHome.adapter = adapter_diary
                binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext()).also {it.orientation = LinearLayoutManager.HORIZONTAL}
            }
        }

        shoppingVM.shoppingList.observe(viewLifecycleOwner){
            adapter_shopping.shoppingList = shoppingVM.ShoppingList
            if (binding.tabLayout2.selectedTabPosition == 1){
                binding.recyclerViewHome.adapter = adapter_shopping
                binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
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
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }


    suspend fun setWeather():Weather{
        val corou = CoroutineScope(Dispatchers.IO).async {
            val response: Weather = WeatherService.client!!.getweather(
                    CurrentUser.location!!,
                    "${BuildConfig.WEATHER_API_KEY}"
            )
            response
        }

        return corou.await()
    }

    suspend fun getDegree():String {
        return setWeather().main.temp.toString()
    }

    suspend fun setExchangeRate(): ExchangeRate {
        val corou = CoroutineScope(Dispatchers.IO).async {
            val response: ExchangeRate = ExchangeRateService.client!!.getExchangeRate(
                    "${BuildConfig.EXCHANGE_RATE_API_KEY}"
            )
            response
        }
        return corou.await()
    }


    suspend fun getExchangeRateCanada(): String{

        val exchangeRate = setExchangeRate()
        for(i in 0..exchangeRate.size-1){
            if ( exchangeRate.get(i).cur_unit == "CAD"){
                return exchangeRate.get(i).deal_bas_r
            }
        }
        return ""
    }


    fun setDday():Int{
        val year: Int = CurrentUser.departure!!.substring(0 until 4).toInt()
        val month: Int = CurrentUser.departure!!.substring(4 until 6).toInt() - 1
        val date: Int = CurrentUser.departure!!.substring(6 until 8).toInt()
        val departureDate = Calendar.getInstance()
        departureDate.set(year,month,date)
        val d = departureDate.timeInMillis

        val today : Calendar = Calendar.getInstance()
        val t = today.timeInMillis

        val dif = (t - d) / (24*60*60*1000)

        val result = dif.toInt() + 1

        return result
    }
}