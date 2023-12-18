package com.example.savemypasswords.storage

import android.content.Context
import androidx.room.Room
import com.example.savemypasswords.storage.models.db.PasswordDTO
import com.example.savemypasswords.storage.models.db.PasswordDAO
import com.example.savemypasswords.storage.models.db.UserDTO
import com.example.savemypasswords.storage.models.db.UsersDao
import kotlinx.coroutines.flow.Flow

private const val DB = "passwords";

class PasswordsRepo private constructor(context: Context) {
    private val db: PasswordsDatabase = Room.databaseBuilder(context.applicationContext, PasswordsDatabase::class.java, DB).build()
    private val usersDao: UsersDao = db.UsersDao();
    private val passwordsDao: PasswordDAO = db.PasswordDAO();

    fun getPasswords(userLogin:String): Flow<List<PasswordDTO>> {
        return  passwordsDao.getUserPasswords(userLogin)
    }

    suspend fun userByLogin(login:String): UserDTO? {
        return usersDao.getUserByLogin(login);
    }

    suspend fun newUser(user: UserDTO) {
        usersDao.insertUser(user);
    }

    suspend fun newPassword(userLogin: String, data:String) {
        passwordsDao.addPassword(PasswordDTO(userLogin, data))
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