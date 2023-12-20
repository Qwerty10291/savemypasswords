package com.example.savemypasswords.storage.models.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "items")
data class ItemDTO(

    val userLogin:String,
    val type:String,
    val data:String,
    @PrimaryKey(true)
    val id:Int = 0
)

@Dao
interface ItemDAO
{
    @Query("select * from items WHERE userLogin = (:userLogin) and type = 'password' ")
    fun getUserPasswords(userLogin: String):Flow<List<ItemDTO>>

    @Query("select * from items WHERE userLogin = (:userLogin) and type = 'card'")
    fun getUserCards(userLogin: String):Flow<List<ItemDTO>>

    @Query("select * from items WHERE userLogin = (:userLogin) and type = 'note'")
    fun getUserNotes(userLogin: String):Flow<List<ItemDTO>>

    @Query("select * from items WHERE userLogin = (:userLogin) and type = (:type)")
    fun getItems(userLogin: String, type:String):Flow<List<ItemDTO>>

    @Update
    suspend fun updateItem(itemDTO: ItemDTO)

    @Insert
    suspend fun addItem(item:ItemDTO)



    @Delete
    suspend fun deleteItem(item: ItemDTO)
}