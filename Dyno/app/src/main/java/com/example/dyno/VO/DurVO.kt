package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.example.dyno.LocalDB.RoomDAO

// DUR = ("질병" + "질병")  or ("질병" + "건강기능식품")
// diseaseName2가 ""일 경우 질+건으로 보고,
// supplementName이 ""일 경우 질+질으로 본다.
// diseaseName2,supplementName 둘다 ""일 수 없다.
@Entity(tableName = "DUR")
class DurVO() : Parcelable {
    @PrimaryKey
    @ColumnInfo(name="date")
    var date:String=""

    @ColumnInfo(name="d_date1")
    var disease1:String=""
    @ColumnInfo(name="d_name1")
    var diseaseName1 : String =""       // 질병명 1

    @ColumnInfo(name="d_date2")
    var disease2:String=""
    @ColumnInfo(name="d_name2")
    var diseaseName2 : String =""       // 질병명 2

    @ColumnInfo(name="s_name")
    var supplementName : String =""     // 건강기능식품명


    var warnMedicineNames1 : ArrayList<String> = arrayListOf()  // 질병명 1의 병용불가 의약품 리스트

    var warnMedicineNames2 : ArrayList<String> = arrayListOf()  // 질병명 1의 병용불가 의약품 리스트
    var durDetail : String = ""        // 병용금기 내용
    var durCheck:Int=0//병용금기 의약품이 있는지 없는지 용으로 있으면 1 없으면 0

    constructor(date:String,disease1:String,diseaseName1 : String, disease2:String,diseaseName2 : String, supplementName : String,durDetail : String,
                warnMedicineNames1 : ArrayList<String>,warnMedicineNames2 : ArrayList<String>,durCheck:Int) : this(){
        this.date=date
        this.disease1=disease1
        this.diseaseName1 = diseaseName1
        this.disease2=disease2
        this.diseaseName2 = diseaseName2
        this.supplementName = supplementName
        this.warnMedicineNames1 = warnMedicineNames1
        this.warnMedicineNames2 = warnMedicineNames2
        this.durDetail = durDetail
        this.durCheck=durCheck
    }


    constructor(parcel: Parcel) : this(parcel.readString()!!,parcel.readString()!!,parcel.readString()!!,parcel.readString()!!, parcel.readString()!!, parcel.readString()!!,parcel.readString()!!,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>,parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(disease1)
        parcel.writeString(diseaseName1)
        parcel.writeString(disease2)
        parcel.writeString(diseaseName2)
        parcel.writeString(supplementName)
        parcel.writeString(durDetail)
        parcel.writeList(warnMedicineNames1)
        parcel.writeList(warnMedicineNames2)
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

@Dao
interface DurDAO : RoomDAO<DurVO> {
    // 키 겹칠때 지금 넣는것으로 대체



}