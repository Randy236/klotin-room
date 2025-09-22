package com.example.mobileapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Aquí se mostrarán los datos del usuario logueado")
    }
}
