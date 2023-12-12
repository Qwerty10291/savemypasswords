package com.example.savemypasswords.storage

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.savemypasswords.security.BCRYPT
import com.example.savemypasswords.storage.models.Password
import com.example.savemypasswords.storage.models.User
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Comment
import java.time.LocalDate

class AppStorage(): ViewModel() {
    private val currentUser = mutableStateOf(User("", ""))
    private val repository:PasswordsRepo = PasswordsRepo.get();

    fun GetCurrentUser():User {
        return currentUser.value
    }

    fun PasswordsList(): Flow<List<Password>> {
        return repository.getPasswords(currentUser.value.login)
    }

    fun Logout() {
        currentUser.value = User("", "");
    }

    suspend fun SetUserAuth(login:String, password: String):Boolean {
        val hashedPassword = repository.userByLogin(login)?.password;
        if (hashedPassword != null && BCRYPT.ValidatePasswordHash(password, hashedPassword)) {
            currentUser.value = User(login, password)
            return true
        }
        return false
    }

    suspend fun SetNewUser(login:String, password:String) {
        val hashedPassword = BCRYPT.HashPassword(password)
        repository.newUser(User(login, hashedPassword))
        currentUser.value = User(login, password)
    }

    suspend fun NewPassword(site:String, login:String, password: String, comment: String) {
        repository.newPassword(Password(currentUser.value.login, login, password, site, comment, LocalDate.now()))
    }
}