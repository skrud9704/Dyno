package com.example.dyno.LocalDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user :User)

    @Query("SELECT * FROM user")
    fun getAll(): List<User>
}