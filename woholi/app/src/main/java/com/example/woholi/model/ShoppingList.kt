package com.example.woholi.model

data class ShoppingList(
        var date: String = "",
        var checks: MutableList<Check> = mutableListOf<Check>()
)
