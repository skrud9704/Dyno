package com.example.dyno.LocalDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dyno.VO.SupplementVO

@Dao
interface RoomDAO<ET> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data : ET)
}