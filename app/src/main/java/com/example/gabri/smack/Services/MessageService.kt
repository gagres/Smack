package com.example.gabri.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gabri.smack.Controllers.App
import com.example.gabri.smack.Model.Channel
import com.example.gabri.smack.Model.Message
import com.example.gabri.smack.Utilities.URL_GET_CHANNELS
import com.example.gabri.smack.Utilities.URL_GET_MESSAGES
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun logout () {
        this.clearChannels()
        this.clearMessages()
    }

    fun clearMessages() {
        this.messages.clear()
    }

    fun clearChannels() {
        this.channels.clear()
    }

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelRequest = object: JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener {response ->
            try {
                for(x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val description = channel.getString("description")
                    val id = channel.getString("_id")

                    val newChannel = Channel(name, description, id)

                    this.channels.add(newChannel)
                }

                complete(true)

            } catch(e: JSONException) {
                Log.e("JSON_ERROR", "EXE: "+ e.localizedMessage)
            }
        }, Response.ErrorListener {error ->
            Log.e("REQUEST_ERROR", "Could not retrieve channels $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(channelRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        this.clearMessages() // Clear all messages of the lastest channel

        val messageRequest = object : JsonArrayRequest(Method.GET, "${URL_GET_MESSAGES}$channelId", null, Response.Listener { response ->
            try {
                for(x in 0 until response.length()) {
                    val message = response.getJSONObject(x)
                    val messageBody = message.getString("messageBody")
                    val channelId = message.getString("channelId")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userAvatarColor = message.getString("userAvatarColor")
                    val id = message.getString("_id")
                    val timestamp = message.getString("timestamp")

                    val newMessage = Message(messageBody, userName, channelId, userAvatar, userAvatarColor, id, timestamp)
                    this.messages.add(newMessage)
                }
                complete(true)
            } catch(e: JSONException) {
                Log.e("JSON_ERROR", "EXE: "+ e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.e("REQUEST_ERROR", "Could not retrieve message: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(messageRequest)
    }
}