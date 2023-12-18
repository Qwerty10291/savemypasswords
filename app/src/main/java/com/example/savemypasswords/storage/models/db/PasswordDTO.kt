package com.example.savemypasswords.storage.models.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "password")
data class PasswordDTO(

    val userLogin:String,
    val data:String,
    @PrimaryKey(true)
    val id:Int = 0
)

@Dao
interface PasswordDAO {
    @Query("select * from password WHERE userLogin = (:userLogin)")
    fun getUserPasswords(userLogin: String):Flow<List<PasswordDTO>>

    @Update
    fun updatePassword(passwordDTO: PasswordDTO)

    @Insert
    suspend fun addPassword(password:PasswordDTO)

    @Delete
    suspend fun deletePassword(password: PasswordDTO)
}