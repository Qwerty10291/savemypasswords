package com.example.savemypasswords.storage

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.savemypasswords.storage.converters.Converters
import com.example.savemypasswords.storage.models.Password
import com.example.savemypasswords.storage.models.PasswordDAO
import com.example.savemypasswords.storage.models.User
import com.example.savemypasswords.storage.models.UsersDao


@Database(entities = [User::class, Password::class], version = 1)
@TypeConverters(Converters::class)
abstract class PasswordsDatabase: RoomDatabase() {
    abstract fun UsersDao():UsersDao
    abstract fun PasswordDAO():PasswordDAO
}