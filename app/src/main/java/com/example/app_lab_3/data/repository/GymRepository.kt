package com.example.app_lab_3.data.repository

import com.example.app_lab_3.data.model.Post
import com.example.app_lab_3.data.model.Message
import com.example.app_lab_3.data.model.User
import com.example.app_lab_3.data.remote.GymApiService

/**
 * El Repositorio es el punto único de acceso a los datos.
 * Aquí decidirás si los datos vienen de la API (Network) o de una base de datos local (Room).
 */
class GymRepository(private val apiService: GymApiService) {

    suspend fun getFeedPosts(): List<Post> {
        return apiService.getPosts()
    }

    suspend fun createNewPost(post: Post): Post {
        return apiService.createPost(post)
    }

    suspend fun sendChatMessage(message: Message): Message {
        return apiService.sendMessage(message)
    }

    suspend fun getUserProfile(userId: String): User {
        return apiService.getUserProfile(userId)
    }
}
