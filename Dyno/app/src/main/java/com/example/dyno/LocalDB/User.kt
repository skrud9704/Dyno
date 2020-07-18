package com.example.dyno.LocalDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User (@PrimaryKey(autoGenerate = true) var id:Long?,
            @ColumnInfo(name = "deviceId") var dId: String,
            @ColumnInfo(name = "userName") var uName:String) {
    constructor() : this(null,"","")


}