package com.example.gabri.smack.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gabri.smack.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginBtnClicked(view: View) {

    }

    fun loginSignupBtnClicked(view: View) {
        val signupIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(signupIntent)
        finish()
    }
}
