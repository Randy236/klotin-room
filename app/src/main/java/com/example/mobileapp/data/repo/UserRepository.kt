package com.example.mobileapp.data.repo

import android.content.Context
import com.example.mobileapp.data.local.dao.UserDao
import com.example.mobileapp.data.local.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao,
                     private val moodDao: Context) {

    fun observeAll(): Flow<List<User>> = dao.observeAll()

    fun search(q: String): Flow<List<User>> = dao.search(q)

    suspend fun get(id: Long) = dao.getById(id)

    suspend fun create(
        sku: String,
        username: String?,
        name: String?,
        lastName: String?,
        email: String,
        roles: String? = null,
        hasProfile: Boolean = false,
        hasMoodToday: Boolean = false
    ): Long {
        val user = User(
            sku = sku.trim(),
            username = username?.trim(),
            name = name?.trim(),
            last_name = lastName?.trim(),
            email = email.trim(),
            roles = roles,
            has_profile = hasProfile,
            has_mood_today = hasMoodToday
        )
        return dao.insertUser(user)
    }

    suspend fun update(
        id: Long,
        sku: String,
        username: String?,
        name: String?,
        lastName: String?,
        email: String,
        roles: String? = null,
        hasProfile: Boolean = false,
        hasMoodToday: Boolean = false
    ) {
        val current = dao.getById(id) ?: return
        dao.update(
            current.copy(
                sku = sku.trim(),
                username = username?.trim(),
                name = name?.trim(),
                last_name = lastName?.trim(),
                email = email.trim(),
                roles = roles,
                has_profile = hasProfile,
                has_mood_today = hasMoodToday,
            )
        )
    }

    suspend fun delete(id: Long) = dao.softDelete(id)

    suspend fun getLastMood(userId: Long) = dao.getLastMood(userId)

    suspend fun getUserByEmail(email: String): User? {
        return dao.getUserByEmail(email)
    }
}
