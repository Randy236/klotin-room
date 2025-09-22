package com.example.mobileapp.data.repo

import android.content.Context
import com.example.mobileapp.data.local.dao.UserMoodDao
import com.example.mobileapp.data.local.entity.UserMood
import kotlinx.coroutines.flow.Flow

class MoodUserRepository(
    private val moodUserDao: UserMoodDao,
    private val context: Context
) {
    // Insertar y devolver el id generado
    suspend fun createMoodUser(moodUser: UserMood): Long {
        return moodUserDao.insert(moodUser)
    }

    // Actualizar
    suspend fun updateMoodUser(moodUser: UserMood) {
        moodUserDao.update(moodUser)
    }

    // Eliminar
    suspend fun deleteMoodUser(moodUser: UserMood) {
        moodUserDao.delete(moodUser)
    }

    // Obtener por ID
    suspend fun getMoodUserById(id: Long): UserMood? {
        return moodUserDao.getById(id)
    }

    // Observar todos en tiempo real
    fun getAllMoodUsers(): Flow<List<UserMood>> {
        return moodUserDao.getAll()
    }
}