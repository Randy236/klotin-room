package com.example.mobileapp.model

data class RoleModel(
    val id: Int? = null,               // opcional
    val name: String? = null,          // opcional
    val guardName: String? = null,     // opcional, nota: camelCase en Kotlin
    val createdAt: String? = null,     // opcional, puede ser null
    val updatedAt: String? = null      // opcional, puede ser null
)
