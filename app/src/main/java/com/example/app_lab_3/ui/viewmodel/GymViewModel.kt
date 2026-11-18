package com.example.app_lab_3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_lab_3.data.model.*
import com.example.app_lab_3.data.repository.GymRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * El ViewModel gestiona el estado de la UI y se comunica con el Repositorio.
 */
class GymViewModel(private val repository: GymRepository) : ViewModel() {

    // --- ESTADO DE AUTENTICACIÓN ---
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    // --- ESTADO DE POSTS ---
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _isLoadingPosts = MutableStateFlow(false)
    val isLoadingPosts: StateFlow<Boolean> = _isLoadingPosts.asStateFlow()

    // --- ESTADO DE CHATS ---
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()

    private val _isLoadingChats = MutableStateFlow(false)
    val isLoadingChats: StateFlow<Boolean> = _isLoadingChats.asStateFlow()

    // --- ESTADO DE MENSAJES (conversación abierta) ---
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoadingMessages = MutableStateFlow(false)
    val isLoadingMessages: StateFlow<Boolean> = _isLoadingMessages.asStateFlow()

    // --- ESTADO DE PERFIL ---
    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    // --- MENSAJE DE ERROR ---
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // --- MENSAJE DE ÉXITO (para recuperación de contraseña u otras tareas) ---
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        // En una app real, verificaríamos un token guardado aquí
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    // --- OPERACIONES DE AUTENTICACIÓN ---
    fun login(request: AuthRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isAuthLoading.value = true
            try {
                val response = repository.login(request)
                _userProfile.value = response.user
                _isLoggedIn.value = true
                _errorMessage.value = null
                onSuccess()
                // Cargar datos iniciales tras login
                fetchPosts()
                fetchChats()
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión: ${e.localizedMessage}"
            } finally {
                _isAuthLoading.value = false
            }
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isAuthLoading.value = true
            try {
                val response = repository.register(request)
                _userProfile.value = response.user
                _isLoggedIn.value = true
                _errorMessage.value = null
                onSuccess()
                fetchPosts()
                fetchChats()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear cuenta: ${e.localizedMessage}"
            } finally {
                _isAuthLoading.value = false
            }
        }
    }

    fun recoverPassword(email: String) {
        if (email.isBlank()) return
        viewModelScope.launch {
            _isAuthLoading.value = true
            try {
                repository.recoverPassword(email)
                _successMessage.value = "Se ha enviado un correo para recuperar tu contraseña"
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al recuperar contraseña: ${e.localizedMessage}"
            } finally {
                _isAuthLoading.value = false
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _userProfile.value = null
        _posts.value = emptyList()
        _chats.value = emptyList()
    }

    // --- OPERACIONES DE POSTS ---
    fun fetchPosts() {
        viewModelScope.launch {
            _isLoadingPosts.value = true
            try {
                val result = repository.getFeedPosts()
                _posts.value = result
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar posts: ${e.localizedMessage}"
            } finally {
                _isLoadingPosts.value = false
            }
        }
    }

    fun createPost(content: String, onSuccess: () -> Unit = {}) {
        if (content.isBlank()) return
        viewModelScope.launch {
            try {
                val newPost = Post(
                    id = "",
                    userId = "me",
                    username = _userProfile.value?.username ?: "Usuario Fitzone",
                    content = content.trim()
                )
                val created = repository.createNewPost(newPost)
                _posts.value = listOf(created) + _posts.value.filter { it.id != created.id }
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear publicación: ${e.localizedMessage}"
            }
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            try {
                val updated = repository.likePost(postId)
                _posts.value = _posts.value.map { if (it.id == postId) updated else it }
            } catch (e: Exception) {
                _errorMessage.value = "Error al dar like: ${e.localizedMessage}"
            }
        }
    }

    // --- OPERACIONES DE CHATS ---
    fun fetchChats() {
        viewModelScope.launch {
            _isLoadingChats.value = true
            try {
                val result = repository.getChats()
                _chats.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar conversaciones: ${e.localizedMessage}"
            } finally {
                _isLoadingChats.value = false
            }
        }
    }

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            _isLoadingMessages.value = true
            try {
                val result = repository.getChatMessages(chatId)
                _messages.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar mensajes: ${e.localizedMessage}"
            } finally {
                _isLoadingMessages.value = false
            }
        }
    }

    fun sendMessage(chatId: String, receiverId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            try {
                val newMsg = Message(
                    id = "",
                    senderId = "me",
                    receiverId = receiverId,
                    text = text.trim()
                )
                val sent = repository.sendChatMessage(newMsg)
                _messages.value = _messages.value + sent
                _chats.value = _chats.value.map { chat ->
                    if (chat.id == chatId || chat.participant.id == receiverId) {
                        chat.copy(lastMessage = text, lastMessageTimestamp = sent.timestamp)
                    } else {
                        chat
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al enviar mensaje: ${e.localizedMessage}"
            }
        }
    }

    // --- OPERACIONES DE USUARIO ---
    fun fetchUserProfile(userId: String = "me") {
        viewModelScope.launch {
            try {
                val user = repository.getUserProfile(userId)
                _userProfile.value = user
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar perfil: ${e.localizedMessage}"
            }
        }
    }
}

class GymViewModelFactory(private val repository: GymRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GymViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GymViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
