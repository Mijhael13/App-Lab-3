package com.example.app_lab_3.data.local

import com.example.app_lab_3.data.model.Post

/**
 * Clase para gestionar la persistencia local (Caché).
 * Aquí implementarías Room para guardar posts y mensajes y que la app funcione offline.
 */
class GymLocalDataSource {
    
    // Ejemplo de guardado local
    fun savePostsLocally(posts: List<Post>) {
        // Lógica de Room
    }

    fun getCachedPosts(): List<Post> {
        return emptyList()
    }
}
