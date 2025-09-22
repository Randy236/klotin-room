package com.example.mobileapp.data.local.dao

import androidx.room.*
import com.example.mobileapp.data.local.entity.UserMood
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMoodDao {
    // Insertar un solo registro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userMood: UserMood): Long

    // Insertar varios registros
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userMoods: List<UserMood>)

    // Actualizar registro
    @Update
    suspend fun update(userMood: UserMood)

    // Eliminar registro
    @Delete
    suspend fun delete(userMood: UserMood)

    // Consultar por ID
    @Query("SELECT * FROM usuarios_modo WHERE id = :id")
    suspend fun getById(id: Long): UserMood?

    // Consultar todos los registros ordenados
    @Query("SELECT * FROM usuarios_modo ORDER BY createdAt DESC")
    fun getAll(): Flow<List<UserMood>>
}
