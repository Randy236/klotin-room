package com.example.mobileapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class SnackbarType {
    SUCCESS, ERROR, WARNING, INFO
}

@Composable
fun CustomSnackbar(
    message: String,
    type: SnackbarType,
    modifier: Modifier = Modifier
) {
    val backgroundColor: Color
    val textColor: Color
    val borderColor: Color

    when (type) {
        SnackbarType.SUCCESS -> {
            backgroundColor = Color(0xFFDFF5E1)
            textColor = Color(0xFF2E7D32)
            borderColor = Color(0xFF2E7D32).copy(alpha = 0.5f)
        }
        SnackbarType.ERROR -> {
            backgroundColor = Color(0xFFFFCDD2)
            textColor = Color(0xFFC62828)
            borderColor = Color.Transparent
        }
        SnackbarType.WARNING -> {
            backgroundColor = Color(0xFFFFE0B2)
            textColor = Color(0xFFEF6C00)
            borderColor = Color.Transparent
        }
        SnackbarType.INFO -> {
            backgroundColor = Color(0xFFBBDEFB)
            textColor = Color(0xFF1565C0)
            borderColor = Color.Transparent
        }
    }

    Text(
        text = message,
        color = textColor,
        fontSize = 16.sp,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}