package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable

class DiseaseVO() : Parcelable{
    var dCode:String=""//질병코드
    var dName:String=""//질병명
    var date:String=""//처방날짜
    var medicines : ArrayList<MedicineVO> = arrayListOf()   // 처방된 약 리스트

    constructor(dCode:String,dName:String,date:String,medicines:ArrayList<MedicineVO>) : this(){
        this.dCode=dCode
        this.dName=dName
        this.date=date
        this.medicines=medicines
    }

    constructor(parcel: Parcel) :
            this(parcel.readString()!!, parcel.readString()!!, parcel.readString()!!,
                parcel.readArrayList(MedicineVO::class.java.classLoader) as ArrayList<MedicineVO>
            )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dCode)
        parcel.writeString(dName)
        parcel.writeString(date)
        parcel.writeList(medicines)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DiseaseVO> {
        override fun createFromParcel(parcel: Parcel): DiseaseVO {
            return DiseaseVO(parcel)
        }

        override fun newArray(size: Int): Array<DiseaseVO?> {
            return arrayOfNulls(size)
        }
    }

}