package com.example.mobileapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mobileapp.data.local.entity.User
import com.example.mobileapp.viewmodel.AuthViewModel

object ProtectedRoutes {

    @Composable
    fun RequireAuth(
        navController: NavHostController,
        authViewModel: AuthViewModel,
        content: @Composable () -> Unit
    ) {
        val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

        LaunchedEffect(currentUser) {
            if (currentUser == null) {
                val current = navController.currentBackStackEntry?.destination?.route
                if (current != ScreenRoute.Login.route) {
                    navController.navigate(ScreenRoute.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        if (currentUser != null) {
            content()
        }
    }
}