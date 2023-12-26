package com.example.savemypasswords.storage

import androidx.lifecycle.ViewModel
import com.example.savemypasswords.security.AES256
import com.example.savemypasswords.security.BCRYPT
import com.example.savemypasswords.security.sha256
import com.example.savemypasswords.storage.models.db.UserDTO
import com.example.savemypasswords.storage.models.dto.Card
import com.example.savemypasswords.storage.models.dto.Identifiable
import com.example.savemypasswords.storage.models.dto.Note
import com.example.savemypasswords.storage.models.dto.Password
import com.example.savemypasswords.storage.models.dto.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import java.time.LocalDate
import java.util.Base64


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
        return repository.getItems(currentUser.login, name).transform {items ->
            emit(items.map {itemEnc ->
                val decrypted = AES256.decrypt(Base64.getDecoder().decode(itemEnc.data), currentUser.key)
                val item = Json.decodeFromString<T>(decrypted.toString(charset = Charsets.UTF_8))
                item.id = itemEnc.id
                item
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

    suspend fun NewCard(number:String, expirity:String, cvc:String, holder:String) {
        val data = Json.encodeToString(Card(number, expirity, cvc, holder));
        val encrypted = AES256.encrypt(data.toByteArray(), currentUser.key)
        repository.newCard(currentUser.login, Base64.getEncoder().encodeToString(encrypted))
    }

    suspend fun UpdateCard(card:Card)  {
        val data = Json.encodeToString(card)
        val encrypted = AES256.encrypt(data.toByteArray(), currentUser.key)
        repository.updateCard(currentUser.login, card.id, Base64.getEncoder().encodeToString(encrypted))
    }

    suspend fun NewNote(header:String, content:String) {
        val data = Json.encodeToString(Note(header, content, LocalDate.now()));
        val encrypted = AES256.encrypt(data.toByteArray(), currentUser.key)
        repository.newNote(currentUser.login, Base64.getEncoder().encodeToString(encrypted))
    }

    suspend fun UpdateNote(note:Note) {
        val data = Json.encodeToString(note);
        val encrypted = AES256.encrypt(data.toByteArray(), currentUser.key)
        repository.updateNote(currentUser.login, note.id, Base64.getEncoder().encodeToString(encrypted))
    }

    suspend fun deleteItem(id:Int) {
        repository.deleteId(id)
    }

    fun initFlows() {
        passwords = itemsList("password")
        cards = itemsList("card")
        notes = itemsList("note")
    }

    fun passwordsFiltered(query:String):Flow<List<Password>> {
        return passwords.map { it -> it.filter { it.login.contains(query) || it.site.contains(query) || it.comment.contains(query)} }
    }
    fun cardsFiltered(query:String):Flow<List<Card>> {
        return cards.map { it -> it.filter { it.number.contains(query) } }
    }

    fun notesFiltered(query:String):Flow<List<Note>> {
        return notes.map { it -> it.filter { it.header.lowercase().contains(query.lowercase()) } }
    }
}