package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.example.dyno.LocalDB.RoomDAO
import com.google.gson.annotations.SerializedName

// DUR = ("질병" + "질병")  or ("질병" + "건강기능식품")
// diseaseName2가 ""일 경우 질+건으로 보고,
// supplementName이 ""일 경우 질+질으로 본다.
// diseaseName2,supplementName 둘다 ""일 수 없다.
@Entity(tableName = "DUR")
class DurVO() : Parcelable {
    @PrimaryKey
    @ColumnInfo(name="date")
    @SerializedName("date")
    var date:String=""

    @ColumnInfo(name="type")
    @SerializedName("type")
    var type:Int=0//의-의이면 1이고 건-의면 2

    @ColumnInfo(name="i_date1")
    @SerializedName("i_date1")
    var itemDate1:String=""

    @ColumnInfo(name="i_name1")
    @SerializedName("i_name1")
    var itemName1 : String =""       // 질병명 1

    @ColumnInfo(name="i_date2")
    @SerializedName("i_date2")
    var itemDate2:String=""

    @ColumnInfo(name="i_name2")
    @SerializedName("i_name2")
    var itemName2 : String =""        // 질병명 2

    @ColumnInfo(name="dur_item1")
    @SerializedName("dur_item1")
    var duritems1 : ArrayList<String> = arrayListOf()  // 질병명 1의 병용불가 의약품 리스트

    @ColumnInfo(name="dur_item2")
    @SerializedName("dur_item2")
    var duritems2 : ArrayList<String> = arrayListOf()

    @ColumnInfo(name="dur_reason")
    @SerializedName("dur_reason")
    var durReason : ArrayList<String> = arrayListOf()     // 건강기능식품명



    constructor(date:String, type:Int, itemDate1 : String, itemName1:String, itemDate2 : String, itemName2 : String,
                duritems1 : ArrayList<String>, duritems2 : ArrayList<String>, durReason:ArrayList<String>) : this(){
        this.date=date
        this.type=type
        this.itemDate1 = itemDate1
        this.itemName1=itemName1
        this.itemDate2 = itemDate2
        this.itemName2 = itemName2
        this.duritems1 = duritems1
        this.duritems2 = duritems2
        this.durReason=durReason
    }


    constructor(parcel: Parcel) : this(
        parcel.readString()!!,parcel.readInt(),parcel.readString()!!,
        parcel.readString()!!, parcel.readString()!!,parcel.readString()!!,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>,
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeInt(type)
        parcel.writeString(itemDate1)
        parcel.writeString(itemName1)
        parcel.writeString(itemDate2)
        parcel.writeString(itemName2)
        parcel.writeList(duritems1)
        parcel.writeList(duritems2)
        parcel.writeList(durReason)
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
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    fun insertDur(durVO:DurVO):Long

    @Query("SELECT * FROM DUR")
    fun getDurList() : List<DurVO>

}