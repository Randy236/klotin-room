package com.example.mobileapp.model

data class UserModel(
    val id: Int? = null,
    val name: String? = null,
    val email: String,
    val password: String? = null,
    val password_confirmation: String? = null,
    val roles: List<String>? = null,   // ✅ siempre es array de strings
    val has_profile: Boolean = false,
    val has_mood_today: Boolean = false,
)


data class AuthResponse(
    val status: Boolean,
    val message: String,
    val token: String,       // 🔹 aquí viene el token
    val user: UserModel
)

data class AuthUserResponse(
    val status: Boolean,
    val user: UserModel
)
