package com.example.mobileapp.data.repo

import android.content.Context
import com.example.mobileapp.data.local.dao.UserProfileDao
import com.example.mobileapp.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

class UserProfileRepository(
    private val userProfileDao: UserProfileDao,
    private val context: Context
) {
    // Insertar y devolver el id generado
    suspend fun createProfileUser(moodUser: UserProfile): Long {
        return userProfileDao.insert(moodUser)
    }

    // Actualizar
    suspend fun updateMoodUser(moodUser: UserProfile) {
        userProfileDao.update(moodUser)
    }

    // Eliminar
    suspend fun deleteMoodUser(moodUser: UserProfile) {
        userProfileDao.delete(moodUser)
    }

    // Obtener por ID
    suspend fun getMoodUserById(id: Long): UserProfile? {
        return userProfileDao.getById(id)
    }

    // Observar todos en tiempo real
    fun getAllMoodUsers(): Flow<List<UserProfile>> {
        return userProfileDao.getAll()
    }
}