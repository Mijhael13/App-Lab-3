package com.example.app_lab_3.data.model

data class Message(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
