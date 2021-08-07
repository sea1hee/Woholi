package com.example.woholi.model

data class Diary (
        val date: String = "",
        val title: String = "",
        var contents: String = "",
        val url :List<String> = mutableListOf<String>()
)