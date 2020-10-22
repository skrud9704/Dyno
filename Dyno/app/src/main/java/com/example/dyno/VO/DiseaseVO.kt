package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.example.dyno.LocalDB.RoomDAO

@Entity(tableName = "Disease")
class DiseaseVO() : Parcelable{
    @ColumnInfo(name="d_code")
    var d_code:String=""//질병코드

    @PrimaryKey
    @ColumnInfo(name="d_date")
    var d_date:String=""//처방날짜

    @ColumnInfo(name="d_name")
    var d_name:String=""//질병명

    @ColumnInfo(name="d_medicines")
    var d_medicines : ArrayList<MedicineVO> = arrayListOf()   // 처방된 약 리스트

    constructor(dCode:String,dName:String,date:String,medicines:ArrayList<MedicineVO>) : this(){
        this.d_code=dCode
        this.d_name=dName
        this.d_date=date
        this.d_medicines=medicines
    }

    constructor(parcel: Parcel) :
            this(parcel.readString()!!, parcel.readString()!!, parcel.readString()!!,
                parcel.readArrayList(MedicineVO::class.java.classLoader) as ArrayList<MedicineVO>
            )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(d_code)
        parcel.writeString(d_name)
        parcel.writeString(d_date)
        parcel.writeList(d_medicines)
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
// 마이페이지>의약품 탭에서 보여지는 정보만 가진 객체
class DiseaseMinimal(val d_name : String,val d_date : String,val d_medicines : ArrayList<MedicineVO>){
    fun getMedicineNames() : String{
        var names = ""
        for(idx in d_medicines.indices){
            names = names.plus(d_medicines[idx].name)
            if(idx!=d_medicines.size-1)
                names = names.plus(",\n")
        }
        return names
    }
}

@Dao
interface DiseaseDAO : RoomDAO<DiseaseVO> {
    // 키 겹칠때 지금 넣는것으로 대체
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDisease(diseaseVO: DiseaseVO): Long

    @Query("SELECT * FROM Disease WHERE d_date=:date")
    fun getDisease(date: String) : DiseaseVO

    @Query("SELECT d_name, d_date, d_medicines FROM Disease")
    fun getDiseaseMinimal() : List<DiseaseMinimal>
}