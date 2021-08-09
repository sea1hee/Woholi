package com.example.woholi.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woholi.model.CheckListCategory
import com.example.woholi.model.Diary
import kotlinx.coroutines.launch

class ShoppingListViewModel: ViewModel() {

    val ShoppingList = mutableListOf<CheckListCategory>()

    private val _shoppingList = MutableLiveData<List<CheckListCategory>>()
    var shoppingList: LiveData<List<CheckListCategory>> = _shoppingList

    val repository = ShoppingListRepository()

    init{
        viewModelScope.launch{
            ShoppingList.addAll(repository.readShoppingList().toMutableList())
            _shoppingList.value = ShoppingList
        }
    }
}