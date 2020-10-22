package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity

// DUR = ("질병" + "질병")  or ("질병" + "건강기능식품")
// diseaseName2가 ""일 경우 질+건으로 보고,
// supplementName이 ""일 경우 질+질으로 본다.
// diseaseName2,supplementName 둘다 ""일 수 없다.
@Entity(tableName = "DUR")
class DurVO() : Parcelable {

    var durDate : String =""

    var disease1 : String =""       // 질병1 PK
    var diseaseName1 : String =""

    var disease2 : String =""       // 질병2 PK (등록날짜)
    var diseaseName2 : String =""

    var supplementName : String =""     // 건강기능식품명 PK


    var warnMedicineIngredients : ArrayList<String> = arrayListOf()  // 질-질 병용불가 주성분 리스트

    var warnSupplementIngredients : ArrayList<String> = arrayListOf()
    var durDetail : String = ""        // 병용금기 내용
    var durCheck:Int=0//병용금기 의약품이 있는지 없는지 용으로 있으면 1 없으면 0

    constructor(diseaseName1 : String, diseaseName2 : String, supplementName : String,durDetail : String,
                warnMedicineNames1 : ArrayList<String>,warnMedicineNames2 : ArrayList<String>,durCheck:Int) : this(){
        this.disease1 = diseaseName1
        this.disease2 = diseaseName2
        this.supplementName = supplementName
        //this.warnMedicineNames1 = warnMedicineNames1
        //this.warnMedicineNames2 = warnMedicineNames2
        this.durDetail = durDetail
        this.durCheck=durCheck
    }


    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readString()!!, parcel.readString()!!,parcel.readString()!!,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>,parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(diseaseName1)
        parcel.writeString(diseaseName2)
        parcel.writeString(supplementName)
        parcel.writeString(durDetail)
        //parcel.writeList(warnMedicineNames1)
        //parcel.writeList(warnMedicineNames2)
        parcel.writeValue(durCheck)
        parcel.writeInt(durCheck)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DurVO> {
        override fun createFromParcel(parcel: Parcel): DurVO {
            return DurVO(parcel)
        }

        override fun newArray(size: Int): Array<DurVO?> {
            return arrayOfNulls(size)
        }
    }

}