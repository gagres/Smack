package com.example.gabri.smack.Controllers

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.gabri.smack.R
import com.example.gabri.smack.Services.AuthService
import com.example.gabri.smack.Services.UserDataService
import com.example.gabri.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)


        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)

        createAvatarImgView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View) {
        var random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImgView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createUserClicked(view: View) {
        this.enableSpinner(true)
        val username = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPassText.text.toString()

        if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(this, email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(this, username, email, userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess) {

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    this.enableSpinner(false)
                                    finish()
                                } else {
                                    this.errorToast("Error on creating user info")
                                }
                            }
                        } else {
                            this.errorToast("Error on login")
                        }
                    }
                } else {
                    this.errorToast("Error on create user login credentials")
                }
            }
        } else {
            Toast.makeText(this, "Make sure username, email and password are filled in.", Toast.LENGTH_SHORT).show()
        }
    }

    fun errorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        this.enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.INVISIBLE
        }
        createAvatarImgView.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
        createUserBtn.isEnabled = !enable
    }

}
