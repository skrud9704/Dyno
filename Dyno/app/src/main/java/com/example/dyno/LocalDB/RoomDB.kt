package com.example.dyno.LocalDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dyno.VO.*

@Database(version = 3, entities = [SupplementVO::class, DiseaseVO::class, DurVO::class,NotRecommendVO::class, DynoDurSupplementVO::class], exportSchema = false)
@TypeConverters(Converter::class)
abstract class RoomDB : RoomDatabase(){

    abstract fun supplementDAO() : SupplementDAO
    abstract fun diseaseDAO() : DiseaseDAO
    abstract fun durDAO():DurDAO
    abstract fun durSupplementDAO():DurSupplementDAO
    abstract fun notRecommmendDAO():NotRecommmendDAO

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