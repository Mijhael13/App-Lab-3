package com.example.app_lab_3.data.model

data class Chat(
    val id: String,
    val participant: User,
    val lastMessage: String,
    val lastMessageTimestamp: Long
)
