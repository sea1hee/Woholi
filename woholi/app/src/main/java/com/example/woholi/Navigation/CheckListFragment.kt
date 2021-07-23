package com.example.woholi.Navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.Navigation.checklist.ShoppingListFragment
import com.example.woholi.Navigation.checklist.TravelItemFragment
import com.example.woholi.R
import com.example.woholi.databinding.FragmentCheckListBinding

class CheckListFragment : Fragment() {

    private var binding : FragmentCheckListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckListBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction().add(R.id.frameLayout_checklist, TravelItemFragment()).commit()

        binding!!.btnShopping.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.frameLayout_checklist, ShoppingListFragment()).commit()
        }
        binding!!.btnTravel.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.frameLayout_checklist, TravelItemFragment()).commit()
            //transaction.replace(R.id.frameLayout_checklist, TravelItemFragment()).commit()
        }
        return binding!!.root
    }

}