package com.example.mobileapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileapp.navigation.NavigationManager
import com.example.mobileapp.ui.components.AuthHeader
import com.example.mobileapp.viewmodel.AuthState
import com.example.mobileapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthContainer(
    authViewModel: AuthViewModel,
    navigationManager: NavigationManager
) {
    var isLogin by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val authState by authViewModel.authState.collectAsStateWithLifecycle() // ✅ ajuste

    // Snackbar para mostrar mensajes de error
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 🚀 Manejo de navegación y errores según estado
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                val user = (authState as AuthState.Success).user
                navigationManager.handleUserNavigation(user)
            }
            is AuthState.Error -> {
                val errorMsg = (authState as AuthState.Error).message
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(errorMsg)
                }
            }
            else -> Unit // Idle o Loading
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AuthHeader(
            companyName = "MoodFlow",
            description = if (isLogin) "Bienvenido a nuestra aplicación" else "Crea tu cuenta para comenzar",
            onShowLogin = { isLogin = true },
            onShowRegister = { isLogin = false },
            isLoginSelected = isLogin
        )

        if (isLogin) {
            LoginScreen(
                navToRegister = { isLogin = false },
                authViewModel = authViewModel
            )
        } else {
            RegisterScreen(
                navToLogin = { isLogin = true },
                authViewModel = authViewModel
            )
        }
    }

    // 👇 SnackbarHost al final para mostrar mensajes
    SnackbarHost(hostState = snackbarHostState) { data ->
        Text(text = data.visuals.message)
    }
}