package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class SupplementVO() : Parcelable{

    @SerializedName("s_name")
    var m_name: String = ""
    @SerializedName("m_company")
    var m_company: String = ""
    @SerializedName("m_date")
    var m_date: String = ""
    var m_ingredients : ArrayList<String> = arrayListOf()           // 기능성원재료
    var m_ingredients_info : ArrayList<String> = arrayListOf()      // 기능성내용, 위 배열과 사이즈가 같아야한다.

    constructor(company: String, name: String, date : String, ingredients : ArrayList<String>, infos : ArrayList<String>) : this() {
        this.m_company = company
        this.m_name=name
        this.m_ingredients=ingredients
        this.m_ingredients_info=infos
        this.m_date = date
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!,parcel.readString()!!,parcel.readString()!!,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(m_name)
        parcel.writeString(m_company)
        parcel.writeString(m_date)
        parcel.writeList(m_ingredients)
        parcel.writeList(m_ingredients_info)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SupplementVO> {
        override fun createFromParcel(parcel: Parcel): SupplementVO {
            return SupplementVO(parcel)
        }

        override fun newArray(size: Int): Array<SupplementVO?> {
            return arrayOfNulls(size)
        }
    }


}