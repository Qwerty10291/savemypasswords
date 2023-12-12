package com.example.savemypasswords.storage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.savemypasswords.storage.models.Password
import com.example.savemypasswords.storage.models.PasswordDAO
import com.example.savemypasswords.storage.models.User
import com.example.savemypasswords.storage.models.UsersDao
import kotlinx.coroutines.flow.Flow

private const val DB = "passwords";

class PasswordsRepo private constructor(context: Context) {
    private val db: PasswordsDatabase = Room.databaseBuilder(context.applicationContext, PasswordsDatabase::class.java, DB).build()
    private val usersDao: UsersDao = db.UsersDao();
    private val passwordsDao:PasswordDAO = db.PasswordDAO();

    fun getPasswords(userLogin:String): Flow<List<Password>> {
        return  passwordsDao.getUserPasswords(userLogin)
    }

    suspend fun userByLogin(login:String):User? {
        return usersDao.getUserByLogin(login);
    }

    suspend fun newUser(user:User) {
        usersDao.insertUser(user);
    }

    suspend fun newPassword(password: Password) {
        passwordsDao.addPassword(password)
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