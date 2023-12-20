package com.example.savemypasswords.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.savemypasswords.NavItems
import com.example.savemypasswords.storage.AppStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PasswordsList():Screen( "passwords_list",true, true) {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Draw(storage: AppStorage, navController: NavHostController, padding: PaddingValues, searchQuery:String, selected:SnapshotStateMap<Int, Unit>) {
        val passwords by storage.passwords.collectAsState(emptyList())
        val clipboardManager: ClipboardManager = LocalClipboardManager.current
        val context = LocalContext.current
        LazyColumn(
            modifier = Modifier.padding(padding),
        ) {
            items(passwords) { item ->
                Divider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth(), color = Color.LightGray
                )
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.combinedClickable(
                        onClick = {
                            if (selected.isNotEmpty()) {
                                if (selected.contains(item.id)) {
                                    selected.remove(item.id)
                                } else {
                                    selected[item.id] = Unit
                                }
                            } else {
                                val pswdData = Json.encodeToString(item)
                                navController.navigate(NavItems.EditPasswordScreen.routeId + "?id=${item.id}&pswd=${pswdData}")
                            }
                        },
                        onLongClick = {
                            if (selected.isEmpty()) {
                                selected[item.id] = Unit
                            }
                        }
                    )) {
                    AsyncImage(
                        modifier = Modifier
                            .height(50.dp)
                            .padding(3.dp),
                        model = "https://${item.site}/favicon.ico",
                        contentDescription = "icon"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(modifier = Modifier, text = item.site)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(modifier = Modifier, text = item.login)
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(item.password))
                        Toast.makeText(context, "Пароль скопирован", Toast.LENGTH_LONG).show()
                    }) {
                        Icon(Icons.Filled.ContentCopy, "copy")
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    if (selected.isNotEmpty()) {
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier
                            .size(30.dp)
                            .border(1.dp, color = Color.LightGray, CircleShape)
                            .clickable {
                                if (selected.contains(item.id)) {
                                    selected.remove(item.id)
                                } else {
                                    selected[item.id] = Unit
                                }
                            }
                            ){
                            if (selected.contains(item.id)) {
                                Icon(Icons.Rounded.Check, "checked")
                            }
                        }
                    }
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