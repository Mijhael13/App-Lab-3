package com.example.app_lab_3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_lab_3.data.model.Post
import com.example.app_lab_3.data.repository.GymRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * El ViewModel gestiona el estado de la UI y se comunica con el Repositorio.
 * Mantiene los datos a través de cambios de configuración (como rotar la pantalla).
 */
class GymViewModel(private val repository: GymRepository) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getFeedPosts()
                _posts.value = result
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createPost(content: String) {
        viewModelScope.launch {
            // Lógica para enviar post al repositorio y actualizar la lista
        }
    }
}
