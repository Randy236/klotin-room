package com.example.mobileapp.model

data class MoodUserModel(
    val id: Int? = null,  // Opcional para edición futura
    var estado: String? = null,
    var descripcion: String? = null,
    val colors: List<String>? = null
)
