package com.example.mobileapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobileapp.data.local.AppDatabase
import com.example.mobileapp.data.repo.AuthRepository
import com.example.mobileapp.data.repo.MoodUserRepository
import com.example.mobileapp.data.repo.UserProfileRepository
import com.example.mobileapp.data.repo.UserRepository
import com.example.mobileapp.ui.screens.*
import com.example.mobileapp.viewmodel.AuthViewModel
import com.example.mobileapp.viewmodel.MoodUserViewModel
import com.example.mobileapp.viewmodel.UserProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val navigationManager = remember { NavigationManager(navController) }

    val db = remember { AppDatabase.getInstance(context) }

    val authRepository = remember { AuthRepository(db.userDao(), context) }
    val userR = remember { UserRepository(db.userDao(), context) }

    val authViewModel = remember { AuthViewModel(authRepository, userR) }

    val userProfileViewModel = remember {
        UserProfileViewModel(UserProfileRepository(db.userProfileDao(), context), authViewModel)
    }
    val moodUserViewModel = remember {
        MoodUserViewModel(MoodUserRepository(db.userMoodDao(), context), authViewModel)
    }

    // 🚀 NavHost
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Splash.route
    ) {
        composable(ScreenRoute.Splash.route) {
            // ⚡ Splash decide la navegación inicial (no RequireAuth)
            SplashScreen(
                authViewModel = authViewModel,
                navigationManager = navigationManager
            )
        }

        // Rutas públicas
        composable(ScreenRoute.Login.route) {
            AuthContainer(authViewModel, navigationManager)
        }

        composable(ScreenRoute.Register.route) {
            AuthContainer(authViewModel, navigationManager)
        }

        // Rutas protegidas
        composable(ScreenRoute.QuizUserProfile.route) {
            ProtectedRoutes.RequireAuth(navController, authViewModel) {
                QuizUserProfileScreen(
                    userProfileViewModel = userProfileViewModel,
                    authViewModel = authViewModel,
                    navigationManager = navigationManager
                )
            }
        }

        composable(ScreenRoute.MoodUser.route) {
            ProtectedRoutes.RequireAuth(navController, authViewModel) {
                MoodUserScreen(
                    moodUserViewModel = moodUserViewModel,
                    authViewModel = authViewModel,
                    navigationManager = navigationManager
                )
            }
        }

        composable(ScreenRoute.Home.route) {
            ProtectedRoutes.RequireAuth(navController, authViewModel) {
                HomeScreen(
                    navToProfile = { navController.navigate(ScreenRoute.Profile.route) },
                    authViewModel,
                    navController
                )
            }
        }

        composable(ScreenRoute.Profile.route) {
            ProtectedRoutes.RequireAuth(navController, authViewModel) {
                UserProfileScreen()
            }
        }
    }
}
