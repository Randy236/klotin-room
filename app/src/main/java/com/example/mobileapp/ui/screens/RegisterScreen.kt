package com.example.mobileapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapp.ui.components.CustomTextField
import com.example.mobileapp.ui.theme.Primary
import com.example.mobileapp.ui.theme.TextSecondary
import com.example.mobileapp.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    navToLogin: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password_confirmation by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp)
    ) {

        Text(
            "Nombre",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "juan carlos peralta"
        )
        Spacer(modifier = Modifier.height(10.dp))

        // --- Email ---
        Text(
            "Correo electrónico",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "example@gmail.com"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // --- Contraseña ---
        Text(
            "Contraseña",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "********",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Confirmar contraseña",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = password_confirmation,
            onValueChange = { password_confirmation = it },
            placeholder = "********",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Primary, // color cuando está seleccionado
                    uncheckedColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Acepto los Términos y Condiciones",
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (acceptTerms) {
                    authViewModel.register(
                        name,
                        email,
                        password,
                        password_confirmation
                    )
                } else {
                    // mostrar mensaje que debe aceptar términos
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
            ),
            contentPadding = PaddingValues(
                vertical = 16.dp,
                horizontal = 8.dp
            )
        ) { Text("Crear cuenta",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ) }

    }
}
