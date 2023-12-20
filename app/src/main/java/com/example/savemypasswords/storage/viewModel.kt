package com.example.savemypasswords.storage

import androidx.lifecycle.ViewModel
import com.example.savemypasswords.screens.PasswordsList
import com.example.savemypasswords.security.AES256
import com.example.savemypasswords.security.BCRYPT
import com.example.savemypasswords.security.sha256
import com.example.savemypasswords.storage.models.db.UserDTO
import com.example.savemypasswords.storage.models.dto.*
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
    lateinit var passwords:Flow<List<Password>>
        private set
    lateinit var cards:Flow<List<Card>>
        private set
    lateinit var notes:Flow<List<Note>>
        private set

    fun GetCurrentUser(): User {
        return currentUser
    }

    private inline fun <reified T:Identifiable> itemsList(name:String): Flow<List<T>> {
        return repository.getItems(currentUser.login, name).transform {pswds ->
            emit(pswds.map {pswd ->
                val decrypted = AES256.decrypt(Base64.getDecoder().decode(pswd.data), currentUser.key)
                val password = Json.decodeFromString<T>(decrypted.toString(charset = Charsets.UTF_8))
                password.id = pswd.id
                password
            } )
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
            initFlows()
            return true
        }
        return false
    }

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
        initFlows()
        return true
    }

    suspend fun NewPassword(site:String, login:String, password: String, comment: String) {
        val data = Json.encodeToString(Password(login, password, site, comment, LocalDate.now()));
        val encrypted = AES256.encrypt(data.toByteArray(), currentUser.key)
        repository.newPassword(currentUser.login, Base64.getEncoder().encodeToString(encrypted))
    }

    suspend fun UpdatePassword(pswd:Password)  {
        val data = Json.encodeToString(pswd)
        val encrypted = AES256.encrypt(data.toByteArray(), currentUser.key)
        repository.updatePassword(currentUser.login, pswd.id, Base64.getEncoder().encodeToString(encrypted))
    }

    suspend fun deleteItem(id:Int) {
        repository.deleteId(id)
    }

    fun initFlows() {
        passwords = itemsList("password")
        cards = itemsList("card")
        notes = itemsList("note")
    }
}