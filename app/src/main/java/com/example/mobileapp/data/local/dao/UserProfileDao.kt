package com.example.mobileapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mobileapp.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    // Insertar un solo registro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProfile: UserProfile): Long

    // Insertar varios registros
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userProfile: List<UserProfile>)

    // Actualizar registro
    @Update
    suspend fun update(userProfile: UserProfile)

    // Eliminar registro
    @Delete
    suspend fun delete(userProfile: UserProfile)

    // Consultar por ID
    @Query("SELECT * FROM usuarios_perfil WHERE id = :id")
    suspend fun getById(id: Long): UserProfile?

    // Consultar todos los registros ordenados
    @Query("SELECT * FROM usuarios_perfil ORDER BY createdAt DESC")
    fun getAll(): Flow<List<UserProfile>>
}