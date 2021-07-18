package com.example.woholi

import android.net.Uri
import java.net.URL

object CurrentUser {
    lateinit var uid: String
    var email: String? = null
    var name : String? = null
    var profile : Uri? = null
    var location: String? = null
    var departure: String? = null
    var birth: String? = null
}
