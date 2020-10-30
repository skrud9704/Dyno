package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.example.dyno.LocalDB.RoomDAO
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Supplement")
class SupplementVO() : Parcelable{

    @PrimaryKey
    @ColumnInfo(name="s_name")
    @SerializedName("s_name")
    var m_name: String = ""

    @ColumnInfo(name="s_company")
    @SerializedName("s_company")
    var m_company: String = ""

    @ColumnInfo(name="s_date")
    @SerializedName("s_date")
    var m_date: String = ""

    @ColumnInfo(name="s_ingredient")
    @SerializedName("s_ingredient")
    var m_ingredients : String =""              // 기능성 원재료

    @ColumnInfo(name="s_info")
    @SerializedName("s_info")
    var m_ingredients_info : String = ""        // 기능성내용, 위 배열과 사이즈가 같아야한다.


    constructor(name: String, company : String, date : String, ingredients : String, infos : String) : this() {
        this.m_company = company
        this.m_name=name
        this.m_ingredients=ingredients
        this.m_ingredients_info=infos
        this.m_date = date
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!,parcel.readString()!!,parcel.readString()!!,
        parcel.readString()!!, parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(m_name)
        parcel.writeString(m_company)
        parcel.writeString(m_date)
        parcel.writeString(m_ingredients)
        parcel.writeString(m_ingredients_info)
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
// 마이페이지>건강기능식품 탭에서 보여지는 정보만 가진 객체
class SupplementMinimal(val s_name : String,val s_date : String, val s_ingredient : String){
    fun getIngredients() : String{
        val list = s_ingredient.split(",")
        var go : String = ""
        for(item in list){
            go = go.plus(item).plus("\n")
        }
        return go
    }
}

@Dao
interface SupplementDAO : RoomDAO<SupplementVO>{
    // 키 겹칠때 지금 넣는것으로 대체 (즉, 같은 이름의 건강기능식품 넣으면 새로 덮어씌워짐!!)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSupplement(supplementVO: SupplementVO): Long

    @Query("SELECT * FROM Supplement WHERE s_name=:name")
    fun getSupplement(name: String) : SupplementVO

    @Query("SELECT s_name, s_date, s_ingredient FROM Supplement")
    fun getSupplementMinimal() : List<SupplementMinimal>
}