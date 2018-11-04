package com.example.gabri.smack.Controllers

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.gabri.smack.R
import com.example.gabri.smack.Services.AuthService
import com.example.gabri.smack.Services.UserDataService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
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
        val username = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPassText.text.toString()

        AuthService.registerUser(this, email, password) { registerSuccess ->
            if (registerSuccess) {
                AuthService.loginUser(this, email, password) { loginSuccess ->
                    if (loginSuccess) {
                        AuthService.createUser(this, username, email, userAvatar, avatarColor) { createSuccess ->
                            if (createSuccess) {
                                println(UserDataService.avatarName)
                                println(UserDataService.avatarColor)
                                println(UserDataService.name)
                                finish()
                            }
                        }
                    }
                }
                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Fail to create user", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
