package com.example.app_lab_3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_lab_3.data.model.Post
import com.example.app_lab_3.ui.components.FitzoneHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen() {
    val posts = remember {
        mutableStateListOf(
            Post("1", "u1", "Juan Perez", "Entrenamiento de hoy completado! #GymLife"),
            Post("2", "u2", "Maria Garcia", "Nueva PR en peso muerto: 100kg!"),
            Post("3", "u3", "Carlos Fit", "Dieta estricta esta semana.")
        )
    }

    var showCreatePost by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF8F9F8),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreatePost = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Post")
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            FitzoneHeader(screenName = "Inicio", subtitle = "Tu comunidad fitness")
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(posts) { post ->
                    PostItem(post)
                }
            }
        }

        if (showCreatePost) {
            CreatePostDialog(
                onDismiss = { showCreatePost = false },
                onPostCreated = { content ->
                    posts.add(0, Post(
                        id = System.currentTimeMillis().toString(),
                        userId = "me",
                        username = "Mi Usuario",
                        content = content
                    ))
                    showCreatePost = false
                }
            )
        }
    }
}

@Composable
fun PostItem(post: Post) {
    var liked by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = post.username,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { liked = !liked }) {
                    Icon(
                        imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (liked) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
                Text(
                    text = "${if (liked) post.likesCount + 1 else post.likesCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun CreatePostDialog(onDismiss: () -> Unit, onPostCreated: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Post", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("¿Qué estás entrenando hoy?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = { onPostCreated(text) },
                enabled = text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Postear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.primary)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}
