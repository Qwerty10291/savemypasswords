package com.example.savemypasswords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savemypasswords.storage.AppStorage
import com.example.savemypasswords.storage.models.dto.Password
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPasswordScreen : NavScreen("new_password") {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Draw(navController: NavController, viewModel: AppStorage) {
        val corutine = rememberCoroutineScope()
        val errorMsg = rememberSaveable {
            mutableStateOf("")
        }
        PasswordForm(site = "", login = "", password = "", comment = "", errorMsg = errorMsg, onSave = {newSite, newLogin, newPassword, newComment ->
            corutine.launch {
                withContext(Dispatchers.IO) {
                    viewModel.NewPassword(newSite, newLogin, newPassword, newComment)
                }
                navController.popBackStack()
            }
        }, onBack = {navController.popBackStack()})
    }
}

class EditPasswordScreen: NavScreen("edit_password") {
    @Composable
    fun Draw(navController: NavController, viewModel: AppStorage, pswd:Password) {
        val corutine = rememberCoroutineScope()
        val errorMsg = rememberSaveable {
            mutableStateOf("")
        }
        PasswordForm(site = pswd.site, login = pswd.login, password = pswd.password, comment = pswd.comment, errorMsg = errorMsg, onSave = {newSite, newLogin, newPassword, newComment ->
            corutine.launch {
                withContext(Dispatchers.IO) {
                    viewModel.UpdatePassword(Password(newLogin, newPassword, newSite, newComment, pswd.created, pswd.id))
                }
                navController.popBackStack()
            }
        }, onBack = {navController.popBackStack()})
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordForm(site:String, login:String, password: String, comment:String, errorMsg:MutableState<String>,  onSave:(String, String, String, String) -> Unit, onBack:() -> Unit) {
    var newSite by rememberSaveable {
        mutableStateOf(site)
    }
    var newLogin by rememberSaveable {
        mutableStateOf(login)
    }
    var newPassword by rememberSaveable {
        mutableStateOf(password)
    }
    var newComment by rememberSaveable {
        mutableStateOf(comment)
    }
    Scaffold(
        topBar = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, "Назад")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Новый пароль", fontSize = 20.sp)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onSave(newSite, newLogin, newPassword, newComment)
            }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = null
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(0.3f), text = "Сайт:")
                TextField(
                    modifier = Modifier.weight(0.7f),
                    value = newSite,
                    onValueChange = { newSite = it })
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(0.3f), text = "Логин:")
                TextField(
                    modifier = Modifier.weight(0.7f),
                    value = newLogin,
                    onValueChange = { newLogin = it })
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(0.3f), text = "Пароль:")
                TextField(
                    modifier = Modifier.weight(0.7f),
                    value = newPassword,
                    onValueChange = { newPassword = it })
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(0.3f), text = "Комментарий:")
                TextField(
                    modifier = Modifier.weight(0.7f),
                    value = newComment,
                    onValueChange = { newComment = it })
            }
            if (errorMsg.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = errorMsg.value, color = Color.Red)
            }
        }
    }
}