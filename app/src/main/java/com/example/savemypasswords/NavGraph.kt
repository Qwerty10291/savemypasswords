package com.example.savemypasswords

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.savemypasswords.storage.AppStorage
import com.example.savemypasswords.storage.models.dto.Card
import com.example.savemypasswords.storage.models.dto.Note
import com.example.savemypasswords.storage.models.dto.Password
import kotlinx.serialization.json.Json

object NavItems {
    val Auth = AuthScreen()
    val Main = MainScreen()
    val NewPasswordScreen = NewPasswordScreen()
    val EditPasswordScreen = EditPasswordScreen()
    val NewCardScreen = NewCardScreen()
    val EditCardScreen = EditCardScreen()
    val NewNoteScreen = NewNoteScreen()
    val EditNoteScreen = EditNoteScreen()
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
        composable(NavItems.NewCardScreen.routeId) {
            NavItems.NewCardScreen.Draw(navController = controller, viewModel = view)
        }
        composable(NavItems.EditPasswordScreen.routeId + "?id={id}&pswd={pswd}", arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            },
            navArgument("pswd") {
                type = NavType.StringType
            }
        )) {
            val pswd = Json.decodeFromString<Password>(it.arguments?.getString("pswd")!!)
            pswd.id = it.arguments?.getInt("id")!!
            NavItems.EditPasswordScreen.Draw(navController = controller, viewModel = view, pswd = pswd)
        }
        composable(NavItems.EditCardScreen.routeId + "?id={id}&card={card}", arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            },
            navArgument("card") {
                type = NavType.StringType
            }
        )) {
            val card = Json.decodeFromString<Card>(it.arguments?.getString("card")!!)
            card.id = it.arguments?.getInt("id")!!
            NavItems.EditCardScreen.Draw(navController = controller, viewModel = view, card = card)
        }
        composable(NavItems.NewNoteScreen.routeId) {
            NavItems.NewNoteScreen.Draw(navController = controller, viewModel = view)
        }
        composable(NavItems.EditNoteScreen.routeId + "?id={id}&note={note}", arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            },
            navArgument("note") {
                type = NavType.StringType
            }
        )) {
            val note = Json.decodeFromString<Note>(it.arguments?.getString("note")!!)
            note.id = it.arguments?.getInt("id")!!
            NavItems.EditNoteScreen.Draw(navController = controller, viewModel = view, note = note)
        }
    }
}