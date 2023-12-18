package com.example.savemypasswords.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.savemypasswords.storage.models.db.converters.Converters
import com.example.savemypasswords.storage.models.db.PasswordDTO
import com.example.savemypasswords.storage.models.db.PasswordDAO
import com.example.savemypasswords.storage.models.db.UserDTO
import com.example.savemypasswords.storage.models.db.UsersDao


@Database(entities = [UserDTO::class, PasswordDTO::class], version = 1)
@TypeConverters(Converters::class)
abstract class PasswordsDatabase: RoomDatabase() {
    abstract fun UsersDao(): UsersDao
    abstract fun PasswordDAO(): PasswordDAO
}