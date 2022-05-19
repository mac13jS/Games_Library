package com.example.gameslibrary.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gameslibrary.model.GameObject

class Communicator: ViewModel() {
    lateinit var key: String
    lateinit var gameObject: GameObject

    @JvmName("setGameObject1")
    fun setGameObject(value: GameObject) {
        gameObject = value
    }

    @JvmName("setKey1")
    fun setKey(value: String) {
        key = value
    }
}