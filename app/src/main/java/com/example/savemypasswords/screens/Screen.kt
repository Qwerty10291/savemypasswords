package com.example.savemypasswords.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.savemypasswords.storage.AppStorage

abstract class Screen(val route:String, val floatingAction:Boolean = false, val supportSearch:Boolean=false) {
    @Composable
    abstract fun Draw(storage: AppStorage, navController: NavHostController, padding:PaddingValues, searchQuery:String)
    @Composable
    open fun FloatingButton(storage: AppStorage, navController: NavHostController){}
}

object Screens {
    val passwordsList = PasswordsList()

}