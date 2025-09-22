package com.example.mobileapp.model

import android.icu.lang.UCharacter


data class ErrorResponse(
    val message: String? = null,
    val statusCode: UCharacter.NumericType? = null,

)
