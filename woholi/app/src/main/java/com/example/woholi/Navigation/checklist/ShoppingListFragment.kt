package com.example.woholi.Navigation.checklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.woholi.R
import com.example.woholi.databinding.FragmentShoppingListBinding

class ShoppingListFragment : Fragment() {

    private var binding: FragmentShoppingListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

}