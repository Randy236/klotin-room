package com.example.mobileapp.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mobileapp.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileapp.data.local.entity.UserProfile
import com.example.mobileapp.navigation.NavigationManager
import com.example.mobileapp.viewmodel.AuthViewModel
import com.example.mobileapp.viewmodel.UserProfileState
import com.example.mobileapp.viewmodel.UserProfileViewModel

@Composable
fun QuizUserProfileScreen(
    userProfileViewModel: UserProfileViewModel,
    authViewModel: AuthViewModel,
    navigationManager: NavigationManager
) {
    val profileState by userProfileViewModel.state.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    // Navegación automática después de guardar el perfil
    LaunchedEffect(profileState, currentUser) {
        if (profileState is UserProfileState.Success && currentUser?.has_profile == true) {
            navigationManager.handleUserNavigation(currentUser)
        }
    }

    val scroll = rememberScrollState()

    // Estados de selección
    var edad by remember { mutableStateOf<String?>(null) }
    var genero by remember { mutableStateOf<String?>(null) }
    var frecuenciaEjercicio by remember { mutableStateOf<String?>(null) }
    var calidadSueno by remember { mutableStateOf<String?>(null) }
    var mejoras by remember { mutableStateOf<String?>(null) }

    val state by userProfileViewModel.state.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            is UserProfileState.Error -> {
                val msg = (state as UserProfileState.Error).message
                println("Error snackbar: $msg")
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(bottom = 80.dp, top = 35.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Row(horizontalArrangement = Arrangement.Start) {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = "Encuesta",
                    tint = TextSecondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Encuesta Inicial",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextSecondary
                )
            }

            // 🔹 Rango de edad
            Text("Rango de edad", style = MaterialTheme.typography.labelLarge)
            QuizOptionsGrid(
                options = listOf("16-24", "25-34", "35-44", "45+"),
                selectedOption = edad,
                onOptionSelected = { edad = it }
            )

            // 🔹 Género
            Text("Género", style = MaterialTheme.typography.labelLarge)
            QuizOptionsGrid(
                options = listOf("Femenino", "Masculino", "No binario", "Prefiero no decir"),
                selectedOption = genero,
                onOptionSelected = { genero = it }
            )

            // 🔹 Frecuencia de ejercicio
            Text("Frecuencia de ejercicio", style = MaterialTheme.typography.labelLarge)
            QuizOptionsGrid(
                options = listOf("Nunca", "1-2 veces/sem", "3-5 veces/sem", "Diario"),
                selectedOption = frecuenciaEjercicio,
                onOptionSelected = { frecuenciaEjercicio = it }
            )

            // 🔹 Calidad del sueño
            Text("Calidad del sueño", style = MaterialTheme.typography.labelLarge)
            QuizOptionsGrid(
                options = listOf("Pobre", "Regular", "Buena", "Excelente"),
                selectedOption = calidadSueno,
                onOptionSelected = { calidadSueno = it }
            )

            // 🔹 Qué mejorar
            Text("¿Qué te gustaría mejorar en tu vida?", style = MaterialTheme.typography.labelLarge)
            QuizOptionsGrid(
                options = listOf("Hábitos", "Estrés", "Ánimo", "Relaciones"),
                selectedOption = mejoras,
                onOptionSelected = { mejoras = it }
            )
        }

        // Botón de enviar encuesta
        Button(
            onClick = {
                val userId = authViewModel.currentUser.value?.id
                if (userId == null) {
                    // ⚠️ Manejar caso de usuario no logueado
                    return@Button
                }

                val profile = UserProfile(
                    user_id = userId, // 👈 aquí le pasas el id del usuario
                    edad = edad?.split("-")?.firstOrNull()?.toIntOrNull(),
                    genero = when (genero) {
                        "Femenino" -> "F"
                        "Masculino" -> "M"
                        "No binario" -> "BINARIO"
                        "Prefiero no decir" -> "OTROS"
                        else -> null
                    },
                    frecuencia_ejercicio = frecuenciaEjercicio,
                    calidad_sueno = when (calidadSueno) {
                        "Pobre" -> "PO"
                        "Regular" -> "RE"
                        "Buena" -> "BU"
                        "Excelente" -> "EX"
                        else -> null
                    },
                    mejoras = when (mejoras) {
                        "Hábitos" -> "HAB"
                        "Estrés" -> "EST"
                        "Ánimo" -> "ANI"
                        "Relaciones" -> "REL"
                        else -> null
                    }
                )
                userProfileViewModel.submitProfile(profile)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Text("Finalizar encuesta")
        }
    }
}

@Composable
fun QuizOptionsGrid(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        options.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { text ->
                    QuizOptionItem(
                        text = text,
                        checked = selectedOption == text,
                        onCheckedChange = { onOptionSelected(text) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun QuizOptionItem(
    text: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(1.dp, LightCoffee, RoundedCornerShape(30.dp))
            .padding(horizontal = 14.dp, vertical = 5.dp)
    ) {
        RadioButton(
            selected = checked,
            colors = RadioButtonDefaults.colors(
                selectedColor = Primary,
                unselectedColor = Color.Gray,
                disabledSelectedColor = Color.LightGray,
                disabledUnselectedColor = Color.LightGray
            ),
            onClick = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = TextCasiOscuro)
    }
}
