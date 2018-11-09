package com.example.gabri.smack.Model

class Channel (val name: String, val description: String, id: String){
    override fun toString(): String {
        return "#$name"
    }
}