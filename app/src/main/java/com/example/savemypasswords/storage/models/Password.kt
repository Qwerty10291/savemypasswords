package com.example.savemypasswords.storage.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.savemypasswords.storage.converters.Converters
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(primaryKeys = ["site", "login"])
data class Password(
    val userLogin:String,
    val login:String,
    val password: String,
    val site:String,
    val comment:String,
    @TypeConverters(Converters::class)
    val created: LocalDate
)

@Dao
interface PasswordDAO {
    @Query("select * from password WHERE userLogin = (:userLogin)")
    fun getUserPasswords(userLogin: String):Flow<List<Password>>

    @Update
    fun updatePassword(password: Password)

    @Insert
    fun addPassword(password: Password)

    @Delete
    fun deletePassword(password: Password)
}