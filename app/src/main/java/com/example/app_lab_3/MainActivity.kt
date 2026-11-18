package com.example.app_lab_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.example.app_lab_3.data.remote.RetrofitClient
import com.example.app_lab_3.data.repository.GymRepository
import com.example.app_lab_3.ui.screens.*
import com.example.app_lab_3.ui.theme.AppLab3Theme
import com.example.app_lab_3.ui.viewmodel.GymViewModel
import com.example.app_lab_3.ui.viewmodel.GymViewModelFactory

class MainActivity : ComponentActivity() {

    private val gymViewModel: GymViewModel by viewModels {
        GymViewModelFactory(GymRepository(RetrofitClient.gymApiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppLab3Theme {
                MainScreen(viewModel = gymViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: GymViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    var authScreen by remember { mutableStateOf("login") }

    if (!isLoggedIn) {
        // Flujo de Autenticación (Login/Registro)
        if (authScreen == "login") {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { authScreen = "register" },
                onLoginSuccess = { /* El ViewModel gestiona el estado */ }
            )
        } else {
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = { authScreen = "login" },
                onRegisterSuccess = { /* El ViewModel gestiona el estado */ }
            )
        }
    } else {
        // App principal una vez logueado
        FitzoneApp(viewModel = viewModel)
    }
}

@Composable
fun FitzoneApp(viewModel: GymViewModel) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Inicio", "Mensajes", "Calendario", "Perfil")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Email, Icons.Filled.CheckCircle, Icons.Filled.Person)

    var activeChat by remember { mutableStateOf<Chat?>(null) }

    if (activeChat != null && selectedItem == 1) {
        BackHandler {
            activeChat = null
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (activeChat == null) {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(icons[index], contentDescription = item) },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
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
                0 -> FeedScreen(viewModel = viewModel)
                1 -> {
                    if (activeChat == null) {
                        ChatListScreen(
                            viewModel = viewModel,
                            onChatSelected = { chat ->
                                activeChat = chat
                            }
                        )
                    } else {
                        ChatScreen(
                            chat = activeChat!!,
                            viewModel = viewModel,
                            onBack = { activeChat = null }
                        )
                    }
                }
                2 -> CalendarScreen()
                3 -> ProfileScreen(viewModel = viewModel)
            }
        }
    }
}
