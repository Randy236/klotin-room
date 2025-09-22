package com.example.mobileapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios_perfil",
    foreignKeys = [
        ForeignKey(
            entity = User::class,              // referencia a la tabla usuarios
            parentColumns = ["id"],            // columna en User
            childColumns = ["user_id"],        // columna en UserProfile
            onDelete = ForeignKey.CASCADE      // si se borra el User, borra también su perfil
        )
    ],
    indices = [Index(value = ["user_id"])]     // 🔹 índice para acelerar búsquedas
)
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val user_id: Long?,   // 👈 clave foránea (nullable)
    var edad: Int? = null,
    var genero: String? = null,
    var frecuencia_ejercicio: String? = null,
    var calidad_sueno: String? = null,
    var mejoras: String? = null,
    val createdAt: String = currentDateTime(),
    val updatedAt: String = currentDateTime()
)
