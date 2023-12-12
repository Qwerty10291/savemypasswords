package com.example.savemypasswords

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.savemypasswords.storage.AppStorage

import java.time.format.DateTimeFormatter

class MainScreen : NavScreen("main") {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Draw(navController: NavController, viewModel: AppStorage) {
        val passwords by viewModel.PasswordsList().collectAsState(emptyList())
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    title = { Text(viewModel.GetCurrentUser().login) },
                    actions = {
                        IconButton(onClick = {
                            viewModel.Logout()
                            navController.navigate(NavItems.Auth.routeId){
                            this.popUpTo(0)
                        } }) {
                            Icon(Icons.Filled.Logout, "")
                        }
                    })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {navController.navigate(NavItems.NewPasswordScreen.routeId)}) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                items(passwords) { item ->
                    Row() {
                        Text(modifier = Modifier.weight(.3f), text = item.site)
                        Text(modifier = Modifier.weight(.3f), text = item.login)
                        Text(
                            modifier = Modifier.weight(.3f), text = item.created.format(
                                DateTimeFormatter.ISO_DATE
                            )
                        )
                    }
                }
            }
        }
    }
}

