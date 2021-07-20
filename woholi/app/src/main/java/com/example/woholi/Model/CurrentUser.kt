package com.example.woholi.Model

import android.net.Uri
import androidx.lifecycle.ViewModel
import java.net.URL

class CurrentUser : ViewModel(){
    companion object {
        lateinit var uid: String
        var email: String? = null
        var name: String? = null
        var englishName: String? = null
        var profile: String? = null
        var location: String? = null
        var departure: String? = null
        var birth: String? = null
    }
}
