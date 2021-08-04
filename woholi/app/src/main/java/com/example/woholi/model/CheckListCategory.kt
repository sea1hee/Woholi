package com.example.woholi.model

data class CheckListCategory(
        var date: String = "",
        var checkListItems: MutableList<CheckListItem> = mutableListOf<CheckListItem>()
)
