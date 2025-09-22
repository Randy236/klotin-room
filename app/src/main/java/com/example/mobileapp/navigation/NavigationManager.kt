package com.example.mobileapp.navigation

import androidx.navigation.NavHostController
import com.example.mobileapp.data.local.entity.User

class NavigationManager(private val navController: NavHostController) {

    fun handleUserNavigation(user: User?) {
        when {
            user == null -> {
                navController.navigate(ScreenRoute.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            !user.has_profile -> {
                navController.navigate(ScreenRoute.QuizUserProfile.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            !user.has_mood_today -> {
                navController.navigate(ScreenRoute.MoodUser.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> {
                navController.navigate(ScreenRoute.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
}
