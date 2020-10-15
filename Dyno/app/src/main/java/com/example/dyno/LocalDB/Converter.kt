package com.example.dyno.LocalDB

import androidx.room.TypeConverter
import com.example.dyno.VO.MedicineVO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        val listType =
            object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromArrayListMedicineVOToJson(list: ArrayList<MedicineVO?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromJsonToArrayListMedicineVO(value: String?): ArrayList<MedicineVO?>? {
        val listType =
            object : TypeToken<ArrayList<MedicineVO?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
}

