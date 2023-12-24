package com.example.savemypasswords

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savemypasswords.storage.AppStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthScreen() : NavScreen("auth") {


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Draw(controller: NavController, viewModel: AppStorage) {
        val loginState = rememberSaveable {
            mutableStateOf("")
        }
        val passwordState = rememberSaveable {
            mutableStateOf("")
        }
        val errorMsg = rememberSaveable {
            mutableStateOf("")
        }
        val corutineScope = rememberCoroutineScope()
        var finished by rememberSaveable {
            mutableStateOf(false)
        }
        LaunchedEffect(finished) {
            if (finished){
                controller.navigate(NavItems.Main.routeId) {
                    this.popUpTo(0)
                }
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "SMP",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold, color = Color.White
                )
                TextField(modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(10.dp),
                    value = loginState.value,
                    onValueChange = { loginState.value = it },
                    label = { Text(text = "username") })
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(10.dp),
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text(text = "password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(modifier = Modifier.fillMaxWidth(0.5f), onClick = {
                    corutineScope.launch {
                        withContext(Dispatchers.IO) {
                            if (viewModel.SetUserAuth(loginState.value, passwordState.value)) {
                                finished = true;
                            } else {
                                errorMsg.value = "неправильный логин или пароль";
                            }
                        }
                    }
                })
                {
                    Text(text = "Войти", textAlign = TextAlign.Center)
                }
                Button(modifier = Modifier.fillMaxWidth(0.5f), onClick = {
                    corutineScope.launch{
                        withContext(Dispatchers.IO) {
                            if(viewModel.SetNewUser(loginState.value, passwordState.value)) {
                                finished = true;
                            } else {
                                errorMsg.value = "Данный логин занят"
                            }
                        }
                    }
                }) {
                    Text(text = "Новый", textAlign = TextAlign.Center)
                }
                if (errorMsg.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = errorMsg.value, color = Color.Red)
                }
            }
        }
    }
}