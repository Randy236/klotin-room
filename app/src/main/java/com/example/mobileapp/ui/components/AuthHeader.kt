package com.example.mobileapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapp.R
import com.example.mobileapp.ui.theme.*
/*import com.example.mobileapp.ui.theme.Primary
import com.example.mobileapp.ui.theme.TextSecondary*/

@Composable
fun AuthHeader(
    logoRes: Int = R.drawable.logo_app,
    companyName: String,
    description: String,
    onShowLogin: () -> Unit,
    onShowRegister: () -> Unit,
    isLoginSelected: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF62A3FC)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "Logo de la empresa",
                modifier = Modifier.size(150.dp).rotate(10f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = companyName,
            style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Seguimiento de ánimo, retos y modtivación",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones para alternar Login / Registro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = onShowRegister,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isLoginSelected) Primary else BackgroundLight,
                    contentColor = if (!isLoginSelected) Color.White else TextSecondary
                ),
                contentPadding = PaddingValues(
                    vertical = 16.dp
                )

            ) {
                Text("Registrarse",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = onShowLogin,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLoginSelected) Primary else BackgroundLight,
                    contentColor = if (isLoginSelected) Color.White else TextSecondary
                ),
                contentPadding = PaddingValues(
                        vertical = 16.dp,
                        horizontal = 8.dp
            )
            ) {
                Text("Iniciar Sesión",
                        fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }


        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Flecha",
                tint = TextSecondary
            )
            Spacer(modifier = Modifier.width(8.dp))          // Espacio entre icono y texto
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

    }
}
