package com.example.woholi.model

data class CheckListCategory(
        var title: String = "",
        var checkListItems: MutableList<CheckListItem> = mutableListOf<CheckListItem>()
)
