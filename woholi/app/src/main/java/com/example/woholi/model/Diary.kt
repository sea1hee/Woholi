package com.example.woholi.model

data class Diary (
        val title: String,
        var contents: String = "",
        val photo :List<String> = mutableListOf<String>()
)