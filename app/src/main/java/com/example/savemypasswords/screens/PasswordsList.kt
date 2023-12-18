package com.example.savemypasswords.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.savemypasswords.NavItems
import com.example.savemypasswords.storage.AppStorage
import java.time.format.DateTimeFormatter

class PasswordsList():Screen( "passwords_list",true, true) {

    @Composable
    override fun Draw(storage: AppStorage, navController: NavHostController, padding: PaddingValues, searchQuery:String) {
        val passwords by (if(searchQuery.isNotEmpty()) storage.PasswordsListFiltered(searchQuery) else storage.PasswordsList()).collectAsState(emptyList())
        LazyColumn(
            modifier = Modifier.padding(padding),
        ) {
            items(passwords) { item ->
                Divider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth(), color = Color.LightGray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier
                            .height(50.dp)
                            .padding(3.dp),
                        model = "https://${item.site}/favicon.ico",
                        contentDescription = "icon"
                    )
                    Text(modifier = Modifier.weight(.25f), text = item.site)
                    Text(modifier = Modifier.weight(.25f), text = item.login)
                    Text(
                        modifier = Modifier.weight(.25f), text = item.created.format(
                            DateTimeFormatter.ISO_DATE
                        )
                    )
                }
            }
        }
    }

    @Composable
    override fun FloatingButton(storage: AppStorage, navController: NavHostController) {
        FloatingActionButton(onClick = {navController.navigate(NavItems.NewPasswordScreen.routeId)}) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}