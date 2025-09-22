package com.example.mobileapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileapp.navigation.ScreenRoute
import com.example.mobileapp.ui.components.BottomNavBar
import com.example.mobileapp.ui.theme.*
import com.example.mobileapp.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    navToProfile: () -> Unit,
    authViewModel: AuthViewModel,
    navController: NavHostController,
) {
    LaunchedEffect(Unit) {
        authViewModel.getUserLogged()
    }

    // ✅ Ahora con lifecycle-aware
    val userData by authViewModel.currentUser.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            BottomNavBar()
        },
        containerColor = Background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = 50.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Text(
                text = "¡Encuesta completa bienvenido, ${userData?.name ?: "Usuario"}! 🎉",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tu experiencia personalizada está lista",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = TextCasiOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hemos creado recomendaciones según tus respuestas.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileRow(
                    name = userData?.name ?: "Usuario",
                    onClick = navToProfile
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = BorderColor)
                SettingRow("Objetivo principal", "Mejorar hábitos y ánimo")
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = BorderColor)
                SettingRow("Recordatorios", "Mañana y tarde")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Primer reto sugerido",
                        fontWeight = FontWeight.Bold,
                        color = TextCasiOscuro
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "📝 Escribe 3 cosas por las que agradeces hoy",
                        fontSize = 14.sp,
                        color = TextCasiOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Te ayudará a iniciar con una mentalidad positiva.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { /* TODO */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Empezar ahora", color = Color.White)
                        }
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = LightCoffee),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, LightCoffee),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Más tarde")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(ScreenRoute.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Cerrar sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Podrás cambiar estas preferencias en Ajustes.",
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ProfileRow(name: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LightCoffee)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Medium, color = TextCasiOscuro)
            Text("Preferencias básicas guardadas", fontSize = 13.sp, color = TextSecondary)
        }
        TextButton(onClick = onClick) {
            Text("Editar", color = Primary)
        }
    }
}

@Composable
fun SettingRow(title: String, subtitle: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, color = TextCasiOscuro)
            Text(subtitle, fontSize = 13.sp, color = TextSecondary)
        }
        TextButton(onClick = { /* TODO */ }) {
            Text("Cambiar", color = Primary)
        }
    }
}
