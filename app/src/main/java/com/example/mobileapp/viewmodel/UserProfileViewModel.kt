package com.example.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.local.entity.UserProfile
import com.example.mobileapp.data.repo.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserProfileState {
    object Idle : UserProfileState()
    object Loading : UserProfileState()
    data class Success(val message: String) : UserProfileState()
    data class Error(val message: String) : UserProfileState()
}

class UserProfileViewModel(
    private val repository: UserProfileRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _state = MutableStateFlow<UserProfileState>(UserProfileState.Idle)
    val state: StateFlow<UserProfileState> get() = _state

    fun submitProfile(profile: UserProfile) {
        viewModelScope.launch {
            _state.value = UserProfileState.Loading
            try {
                // 👇 Jalar el usuario actual del AuthViewModel
                val userId = authViewModel.currentUser.value?.id
                Log.d("UserProfileVM", "👉 currentUser desde AuthViewModel = ${authViewModel.currentUser.value}")
                Log.d("UserProfileVM", "👉 userId obtenido = $userId")

                if (userId == null) {
                    Log.e("UserProfileVM", "❌ Usuario no logueado, no se puede guardar perfil")
                    _state.value = UserProfileState.Error("Usuario no logueado")
                    return@launch
                }

                // 👇 asignar el user_id al perfil antes de guardarlo
                val profileWithUser = profile.copy(user_id = userId)
                Log.d("UserProfileVM", "👉 Perfil a guardar = $profileWithUser")

                val id = repository.createProfileUser(profileWithUser) // este devuelve Long
                Log.d("UserProfileVM", "👉 Resultado insert (rowId) = $id")

                if (id > 0) {
                    _state.value = UserProfileState.Success("Encuesta enviada correctamente")

                    // ⚡ Actualizar al usuario indicando que ya tiene perfil
                    authViewModel.updateCurrentUser { it.copy(has_profile = true) }
                    Log.d("UserProfileVM", "✅ Perfil guardado correctamente, user actualizado")
                } else {
                    Log.e("UserProfileVM", "❌ Error al guardar perfil en Room")
                    _state.value = UserProfileState.Error("No se pudo guardar el perfil")
                }
            } catch (e: Exception) {
                Log.e("UserProfileVM", "❌ Excepción en submitProfile: ${e.localizedMessage}", e)
                _state.value = UserProfileState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun resetProfileState() {
        Log.d("UserProfileVM", "🔄 Reset de estado de perfil")
        _state.value = UserProfileState.Idle
    }
}
