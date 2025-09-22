package com.example.mobileapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapp.ui.components.CustomTextField
import com.example.mobileapp.ui.theme.Primary
import com.example.mobileapp.ui.theme.TextSecondary
import com.example.mobileapp.viewmodel.AuthState
import com.example.mobileapp.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navToRegister: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        // --- Email ---
        Text("Correo electrónico", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "example@gmail.com"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // --- Contraseña ---
        Text("Contraseña", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "********",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Text("Ingresar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 👇 Mostrar errores o cargando
        when (authState) {
            is AuthState.Loading -> Text("Cargando...", color = TextSecondary)
            is AuthState.Error -> Text(
                (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
            else -> {}
        }
    }
}
