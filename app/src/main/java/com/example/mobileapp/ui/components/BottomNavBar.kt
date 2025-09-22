package com.example.mobileapp.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications

@Composable
fun BottomNavBar() {
    var selectedIndex by remember { mutableStateOf(0) }

    val items = listOf(
        NavItem("Ánimo", Icons.Default.Home),
        NavItem("Retos", Icons.Default.List),
        NavItem("Historial", Icons.Default.History),
        NavItem("Notifs", Icons.Default.Notifications)
    )

    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = Color.White // Fondo blanco
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, style = MaterialTheme.typography.labelMedium) },
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent // Sin fondo al seleccionar
                )
            )
        }
    }
}

data class NavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)