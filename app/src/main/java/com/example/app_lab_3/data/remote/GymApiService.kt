package com.example.app_lab_3.data.remote

import com.example.app_lab_3.data.model.Post
import com.example.app_lab_3.data.model.Message
import com.example.app_lab_3.data.model.User
import com.example.app_lab_3.data.model.Chat
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz para definir los puntos de entrada (endpoints) de la API usando Retrofit.
 */
interface GymApiService {
    
    // Posts
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @POST("posts")
    suspend fun createPost(@Body post: Post): Post

    @POST("posts/{id}/like")
    suspend fun likePost(@Path("id") postId: String): Post

    // Mensajería
    @GET("chats")
    suspend fun getChats(): List<Chat>

    @GET("messages/{chatId}")
    suspend fun getMessages(@Path("chatId") chatId: String): List<Message>

    @POST("messages")
    suspend fun sendMessage(@Body message: Message): Message

    // Usuario
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") userId: String): User
}
