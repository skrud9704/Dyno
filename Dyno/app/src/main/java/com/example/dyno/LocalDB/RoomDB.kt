package com.example.dyno.LocalDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dyno.VO.SupplementDAO
import com.example.dyno.VO.SupplementVO

@Database(version = 1, entities = [SupplementVO::class], exportSchema = false)
@TypeConverters(Converter::class)
abstract class RoomDB : RoomDatabase(){
    //abstract fun roomDAO() : RoomDAO
    abstract fun supplementDAO() : SupplementDAO

    // 싱글톤
    companion object{
        private var instance : RoomDB ? = null

        @Synchronized
        fun getInstance(context: Context) : RoomDB{
            if(instance==null){
                instance = Room.databaseBuilder(context.applicationContext, RoomDB::class.java,"dyno_local_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }

        fun destroyInstance(){
            instance = null
        }
    }
}