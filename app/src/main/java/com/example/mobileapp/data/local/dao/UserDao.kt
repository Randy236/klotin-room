package com.example.mobileapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mobileapp.data.local.entity.User
import com.example.mobileapp.data.local.entity.UserMood
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<User>)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getById(id: Long): User?

    @Query("SELECT * FROM usuarios ORDER BY createdAt DESC")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM usuarios WHERE isActive = 1 ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<User>>

    @Query("""
        SELECT * FROM usuarios 
        WHERE isActive = 1 AND (name LIKE '%' || :q || '%' OR sku LIKE '%' || :q || '%')
        ORDER BY updatedAt DESC
    """)
    fun search(q: String): Flow<List<User>>

    @Query("UPDATE usuarios SET isActive = 0, updatedAt = :ts WHERE id = :id")
    suspend fun softDelete(id: Long, ts: Long = System.currentTimeMillis())

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): User?

    @Query("SELECT * FROM usuarios WHERE isActive = 1 LIMIT 1")
    suspend fun getLoggedUser(): User?

    @Query("UPDATE usuarios SET isActive = 0")
    suspend fun logoutAll()

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM usuarios_modo WHERE user_id = :userId ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLastMood(userId: Long): UserMood?
}