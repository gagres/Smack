package com.example.gabri.smack.Controllers

import android.app.Application
import android.util.Log
import com.example.gabri.smack.Utilities.SharedPrefs

class App: Application() {
    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}