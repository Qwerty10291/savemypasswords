package com.example.savemypasswords.storage.models.db

import androidx.room.Dao
import androidx.room.Delete
import  androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "users")
data class UserDTO(
    @PrimaryKey val login:String,
    val encryptedPassword:String,
    val encryptedKey:String
)

@Dao
interface UsersDao {
    @Query("SELECT * FROM users WHERE login = (:login)")
    suspend fun getUserByLogin(login: String): UserDTO?

    @Insert
    suspend fun insertUser(user: UserDTO)

    @Delete
    suspend fun deleteUser(user: UserDTO)
}