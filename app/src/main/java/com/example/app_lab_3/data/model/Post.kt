package com.example.app_lab_3.data.model

data class Post(
    val id: String,
    val userId: String,
    val username: String,
    val content: String,
    val imageUrl: String? = null,
    val likesCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
