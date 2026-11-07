package com.example.app_lab_3.data.model

data class User(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val bio: String = ""
)
