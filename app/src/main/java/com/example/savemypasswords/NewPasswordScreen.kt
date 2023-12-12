package com.example.savemypasswords

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savemypasswords.storage.AppStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPasswordScreen: NavScreen("new_password") {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Draw(navController: NavController, viewModel: AppStorage) {
        var site by remember {
            mutableStateOf("")
        }
        var login by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        var comment by remember {
            mutableStateOf("")
        }
        val corutine = rememberCoroutineScope()
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(0.3f), text = "Сайт:")
                TextField(
                    modifier = Modifier.weight(0.7f),
                    value = site,
                    onValueChange = { site = it })
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(0.3f), text = "Логин:")
                TextField(
                    modifier = Modifier.weight(0.7f),
                    value = login,
                    onValueChange = { login = it })
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(0.3f), text = "Пароль:")
                TextField(
                    modifier = Modifier.weight(0.7f),
                    value = password,
                    onValueChange = { password = it })
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(0.3f), text = "Комментарий:")
                TextField(
                    modifier = Modifier.weight(0.7f),
                    value = comment,
                    onValueChange = { comment = it })
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                corutine.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.NewPassword(site, login, password, comment)
                    }
                    navController.popBackStack()
                }

            }) {
                Text(text = "Сохранить", fontSize = 19.sp)
            }
        }
    }
}