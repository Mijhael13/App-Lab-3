package com.example.app_lab_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.app_lab_3.data.model.Chat
import com.example.app_lab_3.ui.screens.ChatListScreen
import com.example.app_lab_3.ui.screens.ChatScreen
import com.example.app_lab_3.ui.screens.FeedScreen
import com.example.app_lab_3.ui.screens.CalendarScreen
import com.example.app_lab_3.ui.screens.ProfileScreen
import com.example.app_lab_3.ui.theme.AppLab3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppLab3Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Inicio", "Mensajes", "Calendario", "Perfil")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Email, Icons.Filled.CheckCircle, Icons.Filled.Person)

    // Estado para manejar qué chat está abierto actualmente
    var activeChat by remember { mutableStateOf<Chat?>(null) }

    // Intercepta el botón de "Atrás" del sistema si estamos dentro de un chat
    if (activeChat != null && selectedItem == 1) {
        BackHandler {
            activeChat = null
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Ocultamos la barra de navegación al entrar a un chat para más inmersión
            if (activeChat == null) {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(icons[index], contentDescription = item) },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                // Al cambiar de pestaña, cerramos cualquier chat abierto
                                if (index != 1) activeChat = null
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedItem) {
                0 -> FeedScreen()
                1 -> {
                    if (activeChat == null) {
                        // Mostramos la lista de todas las conversaciones (diferentes personas)
                        ChatListScreen(onChatSelected = { chat ->
                            activeChat = chat
                        })
                    } else {
                        // Mostramos la conversación específica seleccionada
                        ChatScreen(
                            chat = activeChat!!,
                            onBack = { activeChat = null }
                        )
                    }
                }
                2 -> CalendarScreen()
                3 -> ProfileScreen()
            }
        }
    }
}
