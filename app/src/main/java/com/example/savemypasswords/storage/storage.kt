package com.example.savemypasswords.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.savemypasswords.storage.models.db.ItemDAO
import com.example.savemypasswords.storage.models.db.ItemDTO
import com.example.savemypasswords.storage.models.db.UserDTO
import com.example.savemypasswords.storage.models.db.UsersDao
import com.example.savemypasswords.storage.models.db.converters.Converters


@Database(entities = [UserDTO::class, ItemDTO::class], version = 1)
@TypeConverters(Converters::class)
abstract class PasswordsDatabase: RoomDatabase() {
    abstract fun UsersDao(): UsersDao
    abstract fun PasswordDAO(): ItemDAO
}