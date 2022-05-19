package com.example.gameslibrary.model

data class GameObject(
    val title: String,
    val developer: String,
    val genre: String,
    val date: String,
    val price: String,
    val userId: String,
    val favourite: Boolean,
    val alreadyPlayed: Boolean
)
