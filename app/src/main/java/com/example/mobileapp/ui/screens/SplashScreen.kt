package com.example.mobileapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mobileapp.navigation.NavigationManager
import com.example.mobileapp.viewmodel.AuthViewModel

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    navigationManager: NavigationManager
) {
    // Observamos el usuario actual
    val currentUser by authViewModel.currentUser.collectAsState()

    // Lanza la verificación del usuario solo una vez
    LaunchedEffect(Unit) {
        authViewModel.getUserLogged()
    }

    // Una vez que el usuario se carga, maneja la navegación
    LaunchedEffect(currentUser) {
        currentUser?.let {
            navigationManager.handleUserNavigation(it)
        }
        // Si currentUser sigue siendo null, también podemos enviar a Login
        if (currentUser == null) {
            navigationManager.handleUserNavigation(null)
        }
    }

    // Indicador de carga mientras se obtiene el usuario
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
