package com.example.woholi.Model

data class ShoppingList(
        val date: String? = null,
        val checks: MutableList<Check> = mutableListOf<Check>()
)
