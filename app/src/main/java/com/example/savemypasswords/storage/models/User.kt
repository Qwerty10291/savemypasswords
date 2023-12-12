package com.example.savemypasswords.storage.models

import androidx.room.Dao
import androidx.room.Delete
import  androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "users")
data class User(
    @PrimaryKey val login:String,
    val password:String,
)

@Dao
interface UsersDao {
    @Query("SELECT * FROM users WHERE login = (:login)")
    suspend fun getUserByLogin(login: String): User?

    @Insert
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

}