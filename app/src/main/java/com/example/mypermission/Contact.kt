package com.example.mypermission

import android.graphics.Bitmap
import java.io.Serializable

class Contact:Serializable {
    var name:String? = null
    var phone:String? = null
    var image: Bitmap? = null


    constructor(name: String?, phone: String?) {
        this.name = name
        this.phone = phone
    }
}