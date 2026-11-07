package com.example.app_lab_3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_lab_3.data.model.Chat
import com.example.app_lab_3.data.model.User
import com.example.app_lab_3.ui.components.FitzoneHeader

@Composable
fun ChatListScreen(onChatSelected: (Chat) -> Unit) {
    val chats = remember {
        mutableStateListOf(
            Chat("1", User("u2", "Maria Garcia", "", "Entrenando duro"), "¡Nos vemos en el gym!", System.currentTimeMillis()),
            Chat("2", User("u3", "Carlos Fit", "", "Coach personal"), "¿Cómo va esa dieta?", System.currentTimeMillis() - 3600000),
            Chat("3", User("u4", "Entrenador Pro", "", ""), "Recuerda estirar bien hoy.", System.currentTimeMillis() - 7200000)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9F8))
    ) {
        FitzoneHeader(
            screenName = "Mensajes",
            subtitle = "Tus conversaciones"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(chats) { chat ->
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)) {
                    ChatItem(chat, onClick = { onChatSelected(chat) })
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        ListItem(
            headlineContent = { Text(chat.participant.username, fontWeight = FontWeight.Bold) },
            supportingContent = { Text(chat.lastMessage, maxLines = 1) },
            leadingContent = {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primary
                ) {}
            },
            trailingContent = {
                Text(
                    text = "12:30",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}
