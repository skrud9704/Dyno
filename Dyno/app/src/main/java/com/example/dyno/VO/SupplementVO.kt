package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable

class SupplementVO() : Parcelable{

    var m_no : String = ""
    var m_name: String = ""
    var m_company: String = ""
    var m_num: Int = 0
    var m_date: String = ""

    constructor(no: String, name: String, company: String) : this() {
        this.m_no=no
        this.m_name=name
        this.m_company=company
    }

    constructor(parcel: Parcel) : this() {
        m_no = parcel.readString()
        m_name = parcel.readString()
        m_company = parcel.readString()
        m_num = parcel.readInt()
        m_date = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(m_no)
        parcel.writeString(m_name)
        parcel.writeString(m_company)
        parcel.writeInt(m_num)
        parcel.writeString(m_date)
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