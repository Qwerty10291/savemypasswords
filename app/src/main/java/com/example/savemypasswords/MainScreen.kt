package com.example.savemypasswords

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.savemypasswords.screens.Screens
import com.example.savemypasswords.storage.AppStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainScreen : NavScreen("main") {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Draw(navController: NavHostController, viewModel: AppStorage) {

        var screenName by rememberSaveable {
            mutableStateOf(Screens.passwordsList.route)
        }
        var searchQuery by rememberSaveable {
            mutableStateOf("")
        }
        val currentScreen = when (screenName) {
            Screens.passwordsList.route -> Screens.passwordsList
            Screens.cardsList.route -> Screens.cardsList
            Screens.notesList.route -> Screens.notesList
            else -> Screens.passwordsList
        }
        val selectedIds = remember {
            mutableStateMapOf<Int, Unit>()
        }

        val coroutine = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    title = { Text(viewModel.GetCurrentUser().login) },
                    actions = {
                        if (currentScreen.supportSearch) {
                            OutlinedTextField(
                                modifier = Modifier.widthIn(200.dp, 300.dp),
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                leadingIcon = {
                                    Icon(Icons.Filled.Search, "")
                                })
                        }
                        IconButton(onClick = {
                            viewModel.Logout()
                            navController.navigate(NavItems.Auth.routeId) {
                                this.popUpTo(0)
                            }
                        }) {
                            Icon(Icons.Filled.Logout, "")
                        }
                    })
            },
            floatingActionButton = {
                if (selectedIds.isNotEmpty()) {
                    FloatingActionButton(contentColor = Color.Red, onClick = {
                        coroutine.launch {
                            withContext(Dispatchers.IO) {
                                selectedIds.keys.forEach { viewModel.deleteItem(it) }
                                selectedIds.clear()
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Add")
                    }
                } else {
                    if (currentScreen.floatingAction) {
                        currentScreen.FloatingButton(viewModel, navController)
                    }
                }

            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = screenName == Screens.passwordsList.route,
                        onClick = {
                            screenName = Screens.passwordsList.route
                            searchQuery = ""
                            selectedIds.clear()
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Key,
                                contentDescription = "passwords"
                            )
                        },
                        label = { Text("passwords") })
                    NavigationBarItem(
                        selected = screenName == Screens.cardsList.route,
                        onClick = {
                            screenName = Screens.cardsList.route
                            searchQuery = ""
                            selectedIds.clear()
                        },
                        icon = {
                            Icon(
                                Icons.Filled.CreditCard,
                                contentDescription = "cards"
                            )
                        },
                        label = { Text("cards") })
                    NavigationBarItem(selected = screenName == Screens.notesList.route,
                        onClick = {
                            screenName = Screens.notesList.route
                            searchQuery = ""
                            selectedIds.clear()
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Notes,
                                contentDescription = "notes"
                            )
                        },
                        label = { Text("notes") })
                }
            }
        ) { innerPadding ->
            currentScreen.Draw(
                storage = viewModel,
                navController = navController,
                padding = innerPadding,
                searchQuery = searchQuery,
                selectedIds
            )
        }
    }
}

