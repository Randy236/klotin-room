package com.example.mobileapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import androidx.room.*

@Entity(
    tableName = "usuarios_modo",
    foreignKeys = [
        ForeignKey(
            entity = User::class,           // 👈 referencia a la entidad User
            parentColumns = ["id"],         // columna de User
            childColumns = ["user_id"],     // columna de esta tabla
            onDelete = ForeignKey.CASCADE   // qué hacer si se borra el user
        )
    ],
    indices = [Index(value = ["user_id"])] // 🔹 recomendado para rendimiento
)
data class UserMood(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val user_id: Long?,   // 👈 clave foránea (nullable)
    var estado: String? = null,
    var descripcion: String? = null,
    val colors: List<String>? = null, // necesitas un TypeConverter
    val createdAt: String = currentDateTime(),
    val updatedAt: String = currentDateTime()
)

