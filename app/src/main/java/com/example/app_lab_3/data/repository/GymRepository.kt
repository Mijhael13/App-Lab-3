package com.example.app_lab_3.data.repository

import com.example.app_lab_3.data.model.*
import com.example.app_lab_3.data.remote.GymApiService

/**
 * El Repositorio es el punto único de acceso a los datos de la app.
 * Conecta las pantallas con la API REST de Next.js.
 */
class GymRepository(private val apiService: GymApiService) {

    // --- Auth ---
    suspend fun login(request: AuthRequest): AuthResponse {
        return apiService.login(request)
    }

    suspend fun register(request: RegisterRequest): AuthResponse {
        return apiService.register(request)
    }

    suspend fun recoverPassword(email: String) {
        return apiService.recoverPassword(mapOf("email" to email))
    }

    // --- Posts ---
    suspend fun getFeedPosts(): List<Post> {
        return apiService.getPosts()
    }

    suspend fun createNewPost(post: Post): Post {
        return apiService.createPost(post)
    }

    suspend fun likePost(postId: String): Post {
        return apiService.likePost(postId)
    }

    // --- Mensajería ---
    suspend fun getChats(): List<Chat> {
        return apiService.getChats()
    }

    suspend fun getChatMessages(chatId: String): List<Message> {
        return apiService.getMessages(chatId)
    }

    suspend fun sendChatMessage(message: Message): Message {
        return apiService.sendMessage(message)
    }

    // --- Usuarios ---
    suspend fun getUserProfile(userId: String): User {
        return apiService.getUserProfile(userId)
    }
}
