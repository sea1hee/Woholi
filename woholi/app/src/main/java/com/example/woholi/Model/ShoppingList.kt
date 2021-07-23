package com.example.woholi.Model

data class ShoppingList(
        var date: String = "",
        var checks: MutableList<Check> = mutableListOf<Check>()
)
