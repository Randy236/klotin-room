
package com.example.mobileapp.navigation

sealed class ScreenRoute(val route: String) {

    object Splash : ScreenRoute("login")

    object Login : ScreenRoute("login")
    object Register : ScreenRoute("register")
    object QuizUserProfile : ScreenRoute("quiz-user-profile")
    object MoodUser : ScreenRoute("mood-user")
    object Home : ScreenRoute("home")
    object Profile : ScreenRoute("profile")
}
