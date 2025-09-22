package com.example.mobileapp.data.repo

import android.content.Context
import com.example.mobileapp.data.local.dao.UserDao
import com.example.mobileapp.data.local.entity.AuthResponse
import com.example.mobileapp.data.local.entity.User
import com.example.mobileapp.utils.TokenManager
import retrofit2.Response

class AuthRepository(
    private val userDao: UserDao,
    private val context: Context
) {
    // 👉 Guardar en Room y devolver el ID generado
    suspend fun insertLocalUser(user: User): Long {
        return userDao.insertUser(user)
    }

    // 👉 Registrar (simulación API)
    suspend fun register(user: User): Response<AuthResponse> {
        // aquí ya NO insertes en Room, solo devuelve el fake response
        val fakeToken = "fake_token_${System.currentTimeMillis()}"
        TokenManager.saveToken(context, fakeToken)

        return Response.success(
            AuthResponse(
                status = true,
                message = "Usuario registrado correctamente",
                token = fakeToken,
                user = user
            )
        )
    }

    suspend fun login(user: User): Response<AuthResponse> {
        val dbUser = userDao.login(user.email, user.password ?: "")
        return if (dbUser != null) {
            userDao.logoutAll()
            val updatedUser = dbUser.copy(isActive = true)
            userDao.update(updatedUser)

            val token = "fake_token_${dbUser.id}"
            TokenManager.saveToken(context, token)

            Response.success(
                AuthResponse(
                    status = true,
                    message = "Login exitoso",
                    token = token,
                    user = updatedUser
                )
            )
        } else {
            Response.success(
                AuthResponse(
                    status = false,
                    message = "Credenciales inválidas",
                    token = "",
                    user = null // 👈 importante, no devolver un usuario inventado
                )
            )
        }
    }

    suspend fun getUserLogged(): Response<User> {
        val user = userDao.getLoggedUser()
        return if (user != null) {
            Response.success(user)
        } else {
            Response.success(null)
        }
    }

    suspend fun logout(): Boolean {
        userDao.logoutAll()
        TokenManager.clearToken(context)
        return true
    }
}

