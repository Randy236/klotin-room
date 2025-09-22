package com.example.mobileapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Función para convertir a formato de fecha legible
fun currentDateTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(Date())
}

@Entity(tableName = "usuarios")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sku: String? = "",
    val username: String? = null,
    val name: String? = null,
    val last_name: String? = null,
    val email: String,
    val password: String? = null,
    val password_confirmation: String? = null,
    val isActive: Boolean = true,
    val roles: String? = null,
    val has_profile: Boolean = false,
    val has_mood_today: Boolean = false,
    val createdAt: String = currentDateTime(),
    val updatedAt: String = currentDateTime()
)

data class AuthResponse(
    val status: Boolean,
    val message: String,
    val token: String,       // 🔹 aquí viene el token
    val user: User?
)

data class AuthUserResponse(
    val status: Boolean,
    val user: User
)
