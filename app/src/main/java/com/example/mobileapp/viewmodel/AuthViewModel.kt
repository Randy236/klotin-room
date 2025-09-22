package com.example.mobileapp.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.local.entity.User
import com.example.mobileapp.data.repo.AuthRepository
import com.example.mobileapp.data.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User?) : AuthState()
    data class Error(val message: String) : AuthState()
}
class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.login(User(email = email, password = password))
                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        val user = authResponse.user

                        // 👇 Comparar contraseña
                        if (user?.password != password) {
                            Log.w("AuthViewModel", "⚠️ Contraseña incorrecta para ${user?.email}")
                            _authState.value = AuthState.Error("Contraseña incorrecta")
                            return@launch
                        }

                        if (user == null) {
                            Log.w("AuthViewModel", "⚠️ Usuario no encontrado con email=$email")
                            _authState.value = AuthState.Error("Usuario no encontrado")
                            return@launch
                        }

                        Log.d("AuthViewModel", "🕒 usuario ID = ${user?.id}")
                        // 👇 Traer el último UserMood asociado a este usuario
                        val lastMood = user?.let { userRepository.getLastMood(it.id) }
                        Log.d("AuthViewModel", "🕒 listar datos del usuario = $lastMood")
                        Log.d("AuthViewModel", "🕒 lastMood.updatedAt = ${lastMood?.updatedAt}")

                        val isUpdatedToday = lastMood?.let { isToday(it.updatedAt) } ?: false

                        val updatedUser = user.copy(
                            has_mood_today = isUpdatedToday
                        )

                        // ⚡ Buscar si ya existe en Room
                        val existingUser = userRepository.getUserByEmail(updatedUser.email)

                        val finalUser = if (existingUser != null) {
                            // 🔄 Actualizar si ya existe
                            val updated = updatedUser.copy(id = existingUser.id)
                            userRepository.update( id = updated.id, sku = updated.sku.toString(), username = updated.username, name = updated.name, lastName = updated.last_name, email = updated.email, roles = updated.roles, hasProfile = updated.has_profile, hasMoodToday = updated.has_mood_today )
                            updated
                        } else {
                            // 🚫 No insertar en login
                            updatedUser
                        }

                        // Actualizar estado en memoria
                        _currentUser.value = finalUser
                        _authState.value = AuthState.Success(finalUser)

                        Log.d(
                            "AuthViewModel",
                            "✅ Login correcto. Usuario actualizado: has_mood_today=${finalUser.has_mood_today}"
                        )

                    } ?: run {
                        _authState.value = AuthState.Error("No se pudo iniciar sesión")
                    }
                } else {
                    _authState.value = AuthState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isToday(dateString: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val parsedDate = LocalDateTime.parse(dateString, formatter)

            val today = LocalDate.now()
            val result = parsedDate.toLocalDate() == today

            // 📌 Debug
            Log.d("AuthViewModel", "🗓️ Comparando: parsed=${parsedDate.toLocalDate()} vs today=$today -> $result")

            result
        } catch (e: Exception) {
            Log.e("AuthViewModel", "❌ Error parseando fecha: $dateString", e)
            false
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.register(
                    User(
                        username = "prueba",
                        last_name = "prueba",
                        roles = "Administrador",
                        name = name,
                        email = email,
                        password = password,
                        password_confirmation = confirmPassword,
                    )
                )
                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        val user = authResponse.user

                        // ⚡ Insertar en Room
                        val localId = authRepository.insertLocalUser(user!!)

                        // ⚡ User actualizado con el id de Room
                        val userWithId = user?.copy(id = localId)

                        _currentUser.value = userWithId
                        _authState.value = AuthState.Success(userWithId)
                    } ?: run {
                        _authState.value = AuthState.Error("No se pudo registrar")
                    }
                } else {
                    _authState.value = AuthState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun getUserLogged() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.getUserLogged()
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _currentUser.value = user
                        _authState.value = AuthState.Success(user)
                    } ?: run {
                        _authState.value = AuthState.Idle
                    }
                }
            } catch (_: Exception) {
                _authState.value = AuthState.Idle
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val success = authRepository.logout()
            _currentUser.value = null
            _authState.value = AuthState.Idle
        }
    }

    fun updateCurrentUser(update: suspend (User) -> User) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val updatedUser = update(user)

            // Persistir en DB usando UserRepository
            userRepository.update(
                id = updatedUser.id,
                sku = updatedUser.sku.toString(),
                username = updatedUser.username,
                name = updatedUser.name,
                lastName = updatedUser.last_name,
                email = updatedUser.email,
                roles = updatedUser.roles,
                hasProfile = updatedUser.has_profile,
                hasMoodToday = updatedUser.has_mood_today
            )

            // Actualizar stateFlow en memoria
            _currentUser.value = updatedUser
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}
