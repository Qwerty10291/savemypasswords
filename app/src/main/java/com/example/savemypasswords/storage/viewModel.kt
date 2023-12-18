package com.example.savemypasswords.storage

import androidx.lifecycle.ViewModel
import com.example.savemypasswords.screens.PasswordsList
import com.example.savemypasswords.security.AES256
import com.example.savemypasswords.security.BCRYPT
import com.example.savemypasswords.security.sha256
import com.example.savemypasswords.storage.models.db.UserDTO
import com.example.savemypasswords.storage.models.dto.Password
import com.example.savemypasswords.storage.models.dto.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import java.time.LocalDate
import java.util.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class AppStorage(): ViewModel() {
    private lateinit var currentUser:User;
    private val repository:PasswordsRepo = PasswordsRepo.get();

    fun GetCurrentUser(): User {
        return currentUser
    }

    fun PasswordsList(): Flow<List<Password>> {
        return repository.getPasswords(currentUser.login).transform {pswds ->
            emit(pswds.map {pswd ->
                val decrypted = AES256.decrypt(Base64.getDecoder().decode(pswd.data), currentUser.key)
                val password = Json.decodeFromString<Password>(decrypted.toString(charset = Charsets.UTF_8))
                password.id = pswd.id
                password
            } )
        }
    }

    fun PasswordsListFiltered(query:String): Flow<List<Password>> {
        return  PasswordsList().transform { pswds ->
            emit(pswds.filter { it.site.contains(query) || it.login.contains(query) || it.comment.contains(query) })
        }
    }

    fun Logout() {
        currentUser = User("", ByteArray(0));
    }

    suspend fun SetUserAuth(login:String, password: String):Boolean {
        val user = repository.userByLogin(login);
        if (user != null && BCRYPT.ValidatePasswordHash(password, user.encryptedPassword)) {
            val key = AES256.decrypt(Base64.getDecoder().decode(user.encryptedKey), password.sha256());
            currentUser = User(login, key);
            return true
        }
        return false
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun SetNewUser(login:String, password:String):Boolean {
        val user = repository.userByLogin(login);
        if (user != null) {
            return  false
        }
        val hashedPassword = BCRYPT.HashPassword(password)
        val random = SecureRandom();
        val bytes = ByteArray(32);
        random.nextBytes(bytes);
        val encryptedKey = AES256.encrypt(bytes, password.sha256())
        repository.newUser(UserDTO(login, hashedPassword, Base64.getEncoder().encodeToString(encryptedKey)))
        currentUser = User(login, bytes)
        return  true
    }

    suspend fun NewPassword(site:String, login:String, password: String, comment: String) {
        val data = Json.encodeToString(Password(login, password, site, comment, LocalDate.now()));
        val encrypted = AES256.encrypt(data.toByteArray(), currentUser.key)
        repository.newPassword(currentUser.login, Base64.getEncoder().encodeToString(encrypted))
    }
}