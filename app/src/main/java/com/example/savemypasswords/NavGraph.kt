package com.example.savemypasswords

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.savemypasswords.storage.AppStorage

object NavItems {
    val Auth = AuthScreen()
    val Main = MainScreen()
    val NewPasswordScreen = NewPasswordScreen()
}

@Composable
fun NavGraph(controller:NavHostController, view:AppStorage) {
    NavHost(navController = controller,  startDestination = NavItems.Auth.routeId) {
        composable(NavItems.Auth.routeId) {
            NavItems.Auth.Draw(controller, view)
        }
        composable(NavItems.Main.routeId) {
            NavItems.Main.Draw(controller, view)
        }
        composable(NavItems.NewPasswordScreen.routeId) {
            NavItems.NewPasswordScreen.Draw(navController = controller, viewModel = view)
        }
    }
}