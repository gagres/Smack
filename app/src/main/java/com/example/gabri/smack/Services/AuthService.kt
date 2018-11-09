package com.example.gabri.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gabri.smack.Controllers.App
import com.example.gabri.smack.Utilities.URL_CREATE_USER
import com.example.gabri.smack.Utilities.URL_GET_USER
import com.example.gabri.smack.Utilities.URL_LOGIN
import com.example.gabri.smack.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    fun logout() {
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
    }

    fun registerUser(email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Method.POST,  URL_REGISTER, Response.Listener {response ->
            println(response)
            complete(true)
        }, Response.ErrorListener {error ->
            println(error)
            Log.e("ERROR_REQUEST", "Could not register the user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(registerRequest)
    }

    fun loginUser(email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener {response ->
            Log.d("RESPONSE", "Request response $response")
            try {
                App.prefs.userEmail = response.getString("user")
                App.prefs.authToken = response.getString("token")
                App.prefs.isLoggedIn = true
                complete(true)
            } catch(e: JSONException) {
                Log.d("JSON_ERROR", "EXC: "+ e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener {error ->
            Log.e("ERROR_REQUEST", "We cannot login the user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(loginRequest)
    }

    fun createUser(name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("email", email)
        jsonObject.put("avatarName", avatarName)
        jsonObject.put("avatarColor", avatarColor)
        val requestBody = jsonObject.toString()

        val createRequest = object: JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->
            println(response)
            try {
                UserDataService.email = response.getString("email")
                UserDataService.name = response.getString("name")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.id = response.getString("_id")
                complete(true)
            } catch(e: JSONException) {
                Log.e("JSON_ERROR", "EXC: "+ e.localizedMessage)
            }
        }, Response.ErrorListener { error ->
            Log.e("ERROR_REQUEST", "Could not add user $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(createRequest)
    }

    fun findUserByEmail(complete: (Boolean) -> Unit) {
        val findUserRequest = object : JsonObjectRequest(Method.GET, "${URL_GET_USER}${App.prefs.userEmail}", null,Response.Listener { response ->
            Log.d("RESPONSE", "User find $response")
            try {
                UserDataService.id = response.getString("_id")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.email = response.getString("email")
                UserDataService.name = response.getString("name")
                complete(true)
            } catch(e: JSONException) {
                Log.e("JSON_ERROR", "We cannot find json key: " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error    ->
            Log.e("REQUEST_ERROR", "Could not find user $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>();
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(findUserRequest)
    }
}