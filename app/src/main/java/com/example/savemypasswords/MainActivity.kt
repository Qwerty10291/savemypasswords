package com.example.savemypasswords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.savemypasswords.storage.AppStorage
import com.example.savemypasswords.storage.PasswordsRepo
import com.example.savemypasswords.ui.theme.SaveMyPasswordsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PasswordsRepo.initialize(this)
        val storage:AppStorage by viewModels();
        setContent {
            SaveMyPasswordsTheme {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(controller = navController, storage)
                }
            }
        }
    }
}
