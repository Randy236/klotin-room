package com.example.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.local.entity.UserMood
import com.example.mobileapp.data.repo.MoodUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MoodUserState {
    object Idle : MoodUserState()
    object Loading : MoodUserState()
    data class Success(val message: String) : MoodUserState()
    data class Error(val message: String) : MoodUserState()
}

class MoodUserViewModel(
    private val repository: MoodUserRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _state = MutableStateFlow<MoodUserState>(MoodUserState.Idle)
    val state: StateFlow<MoodUserState> get() = _state

    fun submitMoodUser(mood: UserMood) {
        viewModelScope.launch {
            _state.value = MoodUserState.Loading
            Log.d("MoodUserViewModel", "📤 Enviando estado de ánimo: $mood")

            try {
                val id = repository.createMoodUser(mood) // devuelve Long (id insertado en Room)
                Log.d("MoodUserViewModel", "✅ Mood insertado con id: $id")

                if (id > 0) { // ✅ Insert exitoso
                    // Actualizar currentUser para reflejar que ya tiene mood del día
                    authViewModel.updateCurrentUser { it.copy(has_mood_today = true) }

                    Log.d("MoodUserViewModel", "✅ has_mood_today actualizado a true")

                    // Cambiar el estado a Success
                    _state.value = MoodUserState.Success("Estado de ánimo enviado correctamente")
                } else {
                    Log.w("MoodUserViewModel", "⚠️ No se pudo guardar el mood (id = $id)")
                    _state.value = MoodUserState.Error("No se pudo guardar el estado de ánimo")
                }
            } catch (e: Exception) {
                Log.e("MoodUserViewModel", "❌ Error al guardar mood", e)
                _state.value = MoodUserState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun resetMoodState() {
        Log.d("MoodUserViewModel", "🔄 Reseteando estado a Idle")
        _state.value = MoodUserState.Idle
    }
}
