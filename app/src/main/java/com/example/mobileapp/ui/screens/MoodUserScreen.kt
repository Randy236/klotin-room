package com.example.mobileapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileapp.data.local.entity.User
import com.example.mobileapp.data.local.entity.UserMood
import com.example.mobileapp.navigation.NavigationManager
import com.example.mobileapp.ui.theme.*
import com.example.mobileapp.viewmodel.AuthViewModel
import com.example.mobileapp.viewmodel.MoodUserState
import com.example.mobileapp.viewmodel.MoodUserViewModel

@Composable
fun MoodUserScreen(
    moodUserViewModel: MoodUserViewModel,
    authViewModel: AuthViewModel,
    navigationManager: NavigationManager
) {
    var estado by remember { mutableStateOf<String?>(null) }
    val note = remember { mutableStateOf(TextFieldValue("")) }

    val moodState by moodUserViewModel.state.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    // Navegación automática después de guardar el perfil
    LaunchedEffect(moodState, currentUser) {
        if (moodState is MoodUserState.Success && currentUser?.has_mood_today == true) {
            navigationManager.handleUserNavigation(currentUser)
        }
    }

    // 🔹 Flag para evitar múltiples navegaciones
    var hasNavigated by remember { mutableStateOf(false) }

    // 🔹 Navegación tras guardar estado de ánimo
/*LaunchedEffect((state as? MoodUserState.Success)?.hashCode()) {
   if (state is MoodUserState.Success && !hasNavigated) {
       currentUser?.let {
           navigationManager.handleUserNavigation(it)
           hasNavigated = true
       }
   }
}*/

// 🔹 Fondo dinámico optimizado
val gradientColors = remember(estado) {
   estado?.uppercase()?.let { moodBackgroundGradients[it] }
       ?: listOf(Background, BackgroundLight)
}

val fondo = remember(gradientColors) {
   Brush.linearGradient(
       colors = gradientColors,
       start = Offset(0f, 0f),
       end = Offset(1000f, 1000f) // Evita infinito
   )
}

// 🔹 Manejo de errores
LaunchedEffect(moodState) {
   if (moodState is MoodUserState.Error) {
       val msg = (moodState as MoodUserState.Error).message
       println("Error snackbar: $msg")
   }
}

Column(
   modifier = Modifier
       .fillMaxSize()
       .background(fondo)
       .padding(vertical = 50.dp, horizontal = 15.dp),
   verticalArrangement = Arrangement.spacedBy(16.dp)
) {
   // 🔹 Título
   Text(
       "Hoy, ¿cómo te sientes?",
       style = MaterialTheme.typography.titleMedium,
       fontWeight = FontWeight.Bold,
       fontSize = 22.sp,
       color = TextSecondary
   )

   // 🔹 Selector de estados de ánimo
   val moods = listOf(
       "😊" to "Feliz",
       "😌" to "Tranquilo",
       "😢" to "Triste",
       "😡" to "Frustrado",
       "😰" to "Ansioso",
       "😴" to "Cansado"
   )

   moods.chunked(3).forEach { rowItems ->
       Row(
           modifier = Modifier.fillMaxWidth(),
           horizontalArrangement = Arrangement.spacedBy(12.dp)
       ) {
           rowItems.forEach { (emoji, label) ->
               key(label) {
                   MoodItem(
                       emoji = emoji,
                       label = label,
                       selected = estado == label,
                       onClick = { estado = label },
                       modifier = Modifier.weight(1f)
                   )
               }
           }
       }
   }

   // 🔹 Frase motivadora
   MotivationalCard(estado)

   // 🔹 Canción recomendada
   SongRecommendationCard()

   NoteInput(
       note = note.value,
       onValueChange = { note.value = it }
   )

   Spacer(modifier = Modifier.weight(1f))

   SaveMoodButton(
       estado = estado,
       descripcion = note.value.text,
       onSave = { mood ->
           moodUserViewModel.submitMoodUser(mood)
           // 👇 Limpieza tras guardar
           estado = null
           note.value = TextFieldValue("")
       },
       currentUser = currentUser
   )
}
}

@Composable
fun MoodItem(
emoji: String,
label: String,
selected: Boolean,
onClick: (String) -> Unit,
modifier: Modifier = Modifier
) {
val borderColor = if (selected) Primary else LightCoffee
val backgroundColor = if (selected) Primary.copy(alpha = 0.1f) else Color.Transparent

Column(
   modifier = modifier
       .border(2.dp, borderColor, RoundedCornerShape(16.dp))
       .background(backgroundColor, RoundedCornerShape(16.dp))
       .clickable { onClick(label) }
       .padding(vertical = 16.dp),
   horizontalAlignment = Alignment.CenterHorizontally
) {
   Text(
       emoji,
       style = MaterialTheme.typography.headlineMedium,
       color = if (selected) Primary else Color.Black
   )
   Spacer(modifier = Modifier.height(4.dp))
   Text(
       label,
       style = MaterialTheme.typography.bodySmall,
       color = if (selected) Primary else Color.Gray,
       fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
   )
}
}

// 🔹 Frases motivacionales
val motivationalPhrases = mapOf(
"Feliz" to "Sigue brillando, tu alegría es contagiosa ✨",
"Tranquilo" to "La paz interior es tu mayor fortaleza 🌊",
"Triste" to "Este momento pasará, eres más fuerte de lo que piensas 💪",
"Frustrado" to "La perseverancia siempre da frutos 🌟",
"Ansioso" to "Tu mente es fuerte, tú controlas tus pensamientos 💭",
"Cansado" to "Descansa, tu cuerpo también merece cuidado 😴"
)

@Composable
private fun MotivationalCard(estado: String?) {
val phrase by remember(estado) {
   mutableStateOf(motivationalPhrases[estado] ?: "Sigue adelante, un pequeño paso cuenta.")
}

Card(
   modifier = Modifier
       .fillMaxWidth()
       .border(1.dp, LightCoffee, RoundedCornerShape(20.dp)),
   shape = RoundedCornerShape(20.dp),
   colors = CardDefaults.cardColors(containerColor = Color.White)
) {
   Column(modifier = Modifier.padding(16.dp)) {
       Text(
           "Frase motivadora",
           style = MaterialTheme.typography.bodyMedium,
           fontSize = 18.sp,
           fontWeight = FontWeight.Bold
       )
       Spacer(modifier = Modifier.height(8.dp))
       Text("“$phrase”")
   }
}
}

@Composable
private fun SongRecommendationCard() {
Card(
   modifier = Modifier.fillMaxWidth(),
   shape = RoundedCornerShape(20.dp),
   elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
   colors = CardDefaults.cardColors(containerColor = Color.White)
) {
   Row(
       modifier = Modifier
           .fillMaxWidth()
           .padding(14.dp),
       verticalAlignment = Alignment.CenterVertically,
       horizontalArrangement = Arrangement.SpaceBetween
   ) {
       Box(
           modifier = Modifier
               .size(56.dp)
               .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
               .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
           contentAlignment = Alignment.Center
       ) {
           Icon(
               imageVector = Icons.Default.MoreVert,
               contentDescription = "Portada",
               tint = Color(0xFF9C6B44)
           )
       }

       Spacer(modifier = Modifier.width(12.dp))

       Column(modifier = Modifier.weight(1f)) {
           Text("Sunrise Vibes", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF1E1E1E))
           Text("Artista Desconocido", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
       }

       IconButton(
           onClick = { /* TODO reproducir */ },
           modifier = Modifier
               .size(48.dp)
               .background(Primary, RoundedCornerShape(50))
       ) {
           Icon(
               Icons.Default.PlayArrow,
               contentDescription = "Play",
               tint = Color.White,
               modifier = Modifier.size(28.dp)
           )
       }
   }
}
}

@Composable
private fun NoteInput(
note: TextFieldValue,
onValueChange: (TextFieldValue) -> Unit
) {
Column {
   Text(
       "¿Por qué te sientes así?",
       style = MaterialTheme.typography.titleMedium,
       fontWeight = FontWeight.Bold,
       fontSize = 15.sp,
       color = TextSecondary
   )
   Spacer(modifier = Modifier.height(8.dp))
   OutlinedTextField(
       value = note,
       onValueChange = onValueChange,
       modifier = Modifier
           .fillMaxWidth()
           .height(100.dp),
       placeholder = { Text("Escribe una nota…") },
       shape = RoundedCornerShape(12.dp),
       colors = OutlinedTextFieldDefaults.colors(
           focusedBorderColor = LightCoffee,
           unfocusedBorderColor = LightCoffee,
           focusedContainerColor = Color.White,
           unfocusedContainerColor = Color.White,
           focusedTextColor = Color(0xFF1A1A1A),   // texto cuando está enfocado
           unfocusedTextColor = Color(0xFF2E2E2D), // texto cuando no está enfocado
           cursorColor = LightCoffee                // opcional: color del cursor
       )
   )
}
}

@Composable
private fun SaveMoodButton(
estado: String?,
descripcion: String,
onSave: (UserMood) -> Unit,
currentUser: User?
) {
    Button(
       onClick = {
           val userId = currentUser?.id ?: return@Button

           val moodColors = mapOf(
               "FELIZ" to listOf("#FFD700", "#FFA500"),
               "TRANQUILO" to listOf("#87CEEB", "#00BFFF"),
               "TRISTE" to listOf("#1E90FF", "#0000CD"),
               "FRUSTRADO" to listOf("#FF4500", "#FF6347"),
               "ANSIOSO" to listOf("#8A2BE2", "#4B0082"),
               "CANSADO" to listOf("#A9A9A9", "#696969")
           )

           val estadoNormalizado = when (estado) {
               "Feliz" -> "FELIZ"
               "Tranquilo" -> "TRANQUILO"
               "Triste" -> "TRISTE"
               "Frustrado" -> "FRUSTRADO"
               "Ansioso" -> "ANSIOSO"
               "Cansado" -> "CANSADO"
               else -> null
           }

           val moodUser = UserMood(
               user_id = userId,
               estado = estadoNormalizado,
               descripcion = descripcion,
               colors = estadoNormalizado?.let { moodColors[it] }
           )

           onSave(moodUser)
       },
       modifier = Modifier
           .fillMaxWidth()
           .height(52.dp),
       shape = RoundedCornerShape(26.dp),
       colors = ButtonDefaults.buttonColors(containerColor = Primary)
    ) {
       Text("Guardar estado de ánimo")
    }
}
