package com.example.woholi.ui.checklist

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.woholi.model.CurrentUser
import com.example.woholi.ui.checklist.shopping.ShoppingListFragment
import com.example.woholi.ui.checklist.travel.TravelItemFragment
import com.example.woholi.R
import com.example.woholi.databinding.FragmentCheckListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class CheckListFragment : Fragment() {

    private var binding : FragmentCheckListBinding? = null

    private val travelFragment by lazy { TravelItemFragment() }
    private val shoppingFragment by lazy { ShoppingListFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckListBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction().add(R.id.frameLayout_checklist, travelFragment).commit()
        binding!!.fabAdd.visibility = View.INVISIBLE

        binding!!.btnShopping.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.frameLayout_checklist, shoppingFragment).commit()
            binding!!.fabAdd.visibility = View.VISIBLE
        }
        binding!!.btnTravel.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.frameLayout_checklist, travelFragment).commit()
            binding!!.fabAdd.visibility = View.INVISIBLE
            //transaction.replace(R.id.frameLayout_checklist, TravelItemFragment()).commit()
        }

        binding!!.fabAdd.setOnClickListener {
            //(activity as MainActivity).setFlag(5)
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.dialog_shopping, null)
            val data = HashMap<String, Any>()
            builder.setView(dialogView)
                    .setPositiveButton("확인") { dialogInterface, i ->
                        val newCategory = dialogView.findViewById<EditText>(R.id.category).text.toString()
                        Firebase.firestore.collection("users").document(CurrentUser.uid)
                                .collection("checklist").document("shoppinglist").collection("first")
                                .document(newCategory).set(data)

                        Firebase.firestore.collection("users").document(CurrentUser.uid)
                                .collection("checklist").document("shoppinglist").collection("first")
                                .document(newCategory).collection("second")
                                .document("newCategory!").set(hashMapOf("isChecked" to true))

                        childFragmentManager.beginTransaction().replace(R.id.frameLayout_checklist, ShoppingListFragment()).commit()
                        binding!!.fabAdd.visibility = View.VISIBLE
                    }
                    .show()
        }
        return binding!!.root
    }
}