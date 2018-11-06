package com.example.gabri.smack.Controllers

import android.content.BroadcastReceiver
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.gabri.smack.R
import com.example.gabri.smack.Services.AuthService
import com.example.gabri.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val showSpinner = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginBtnClicked(view: View) {
        val email = loginEmailText.text.toString()
        val password = loginPassText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) {userFound ->
                        if (userFound) {
                            val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                            LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                            finish()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "You need pass email and password", Toast.LENGTH_SHORT).show()
        }
    }

    fun loginSignupBtnClicked(view: View) {
        val signupIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(signupIntent)
        finish()
    }

    fun enableSpinner() {
        if (showSpinner == false) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }

        loginLoginBtn.isEnabled = !showSpinner
        loginSignupBtn.isEnabled = !showSpinner
    }
}
