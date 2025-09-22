package com.example.mobileapp.model

data class UserProfileModel(
    val id: Int? = null,  // Opcional para edición futura
    var edad: Int? = null,
    var genero: String? = null,
    var frecuencia_ejercicio: String? = null,
    var calidad_sueno: String? = null,
    var mejoras: String? = null
)

