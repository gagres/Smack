package com.example.gabri.smack.Services

import android.graphics.Color
import android.os.Message
import android.util.Log
import java.util.*

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""

        AuthService.logout()
        MessageService.logout()
    }

    fun returnAvatarColor(components: String) : Int {
        val strippedColor = components
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")

        var r = 0
        var g = 0
        var b = 0

        val reg = Regex(" ")
        val listColors = strippedColor.split(reg)

        r = (listColors[0].toDouble() * 255.0).toInt()
        g = (listColors[1].toDouble() * 255.0).toInt()
        b = (listColors[2].toDouble() * 255.0).toInt()

        return Color.rgb(r, g, b)
    }
}