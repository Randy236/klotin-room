package com.example.mobileapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobileapp.data.local.dao.UserDao
import com.example.mobileapp.data.local.dao.UserMoodDao
import com.example.mobileapp.data.local.dao.UserProfileDao
import com.example.mobileapp.data.local.entity.User
import com.example.mobileapp.data.local.entity.UserMood
import com.example.mobileapp.data.local.entity.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Database(
    entities = [User::class, UserMood::class, UserProfile::class], // 👈 ahora las 3 tablas
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // 👈 agregar aquí
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userMoodDao(): UserMoodDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun currentDateTime(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return formatter.format(Date())
        }

        fun yesterdayDateTime(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1) // 👈 restar 1 día
            return formatter.format(calendar.time)
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)

                                // Timestamps explícitos usando currentDateTime()
                                val yesterday = yesterdayDateTime()

                                // 1️⃣ Usuario por defecto
                                val defaultUserId = database.userDao().insertUser(
                                    User(
                                        sku = "SKU001",
                                        username = "admin",
                                        name = "Usuario 1",
                                        last_name = "admin",
                                        email = "admin@gmail.com",
                                        password = "admin123",
                                        roles = "Administrador",
                                        has_profile = true,
                                        has_mood_today = false,
                                        isActive = true,
                                        createdAt = yesterday,
                                        updatedAt = yesterday
                                    )
                                )

                                // 2️⃣ Perfil del usuario
                                database.userProfileDao().insert(
                                    UserProfile(
                                        user_id = defaultUserId,
                                        edad = 16,
                                        genero = "M",
                                        frecuencia_ejercicio = "Nunca",
                                        calidad_sueno = "RE",
                                        mejoras = "HAB",
                                        createdAt = yesterday,
                                        updatedAt = yesterday
                                    )
                                )

                                // 3️⃣ Estado de ánimo del usuario
                                database.userMoodDao().insert(
                                    UserMood(
                                        user_id = defaultUserId,
                                        estado = "FELIZ",
                                        descripcion = "Usuario inicial en buen estado",
                                        colors = listOf("#FFD700", "#FFA500"),
                                        createdAt = yesterday,
                                        updatedAt = yesterday
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
