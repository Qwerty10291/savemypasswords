package com.example.savemypasswords.storage

import android.content.Context
import androidx.room.Room
import com.example.savemypasswords.storage.models.db.ItemDAO
import com.example.savemypasswords.storage.models.db.ItemDTO
import com.example.savemypasswords.storage.models.db.UserDTO
import com.example.savemypasswords.storage.models.db.UsersDao
import kotlinx.coroutines.flow.Flow

private const val DB = "items";

class PasswordsRepo private constructor(context: Context) {
    private val db: PasswordsDatabase = Room.databaseBuilder(context.applicationContext, PasswordsDatabase::class.java, DB).build()
    private val usersDao: UsersDao = db.UsersDao();
    private val passwordsDao: ItemDAO = db.PasswordDAO();

    suspend fun userByLogin(login:String): UserDTO? {
        return usersDao.getUserByLogin(login);
    }

    suspend fun newUser(user: UserDTO) {
        usersDao.insertUser(user);
    }

    fun getItems(userLogin:String, type:String): Flow<List<ItemDTO>>{
        return  passwordsDao.getItems(userLogin, type)
    }

    fun getPasswords(userLogin:String): Flow<List<ItemDTO>> {
        return  passwordsDao.getUserPasswords(userLogin)
    }

    fun getCards(userLogin: String):Flow<List<ItemDTO>>{
        return  passwordsDao.getUserCards(userLogin)
    }

    fun getNotes(userLogin: String):Flow<List<ItemDTO>>{
        return  passwordsDao.getUserNotes(userLogin)
    }
    suspend fun newPassword(userLogin: String, data:String) {
        passwordsDao.addItem(ItemDTO(userLogin, "password", data))
    }

    suspend fun updatePassword(userLogin: String, id:Int, data:String) {
        passwordsDao.updateItem(ItemDTO(userLogin, "password", data, id))
    }

    suspend fun newCard(userLogin: String, data:String) {
        passwordsDao.addItem(ItemDTO(userLogin, "card", data))
    }

    suspend fun updateCard(userLogin: String, id:Int, data:String) {
        passwordsDao.updateItem(ItemDTO(userLogin, "card", data, id))
    }

    suspend fun newNote(userLogin: String, data:String) {
        passwordsDao.addItem(ItemDTO(userLogin, "note", data))
    }

    suspend fun updateNote(userLogin: String, id:Int, data:String) {
        passwordsDao.updateItem(ItemDTO(userLogin, "note", data, id))
    }

    suspend fun deleteId(id:Int) {
        passwordsDao.deleteItem(ItemDTO("", "", "", id))
    }

    companion object {
        private var INSTANCE: PasswordsRepo? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PasswordsRepo(context)
            }
        }
        fun get(): PasswordsRepo {
            return INSTANCE ?:
            throw IllegalStateException("not initialized")
        }
    }

}