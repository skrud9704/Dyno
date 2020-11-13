package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.example.dyno.LocalDB.RoomDAO
import com.google.gson.annotations.SerializedName

@Entity(tableName="DurSupplement")
class DynoDurSupplementVO():Parcelable {
    //서버에 있는 건의 병용금기 정보를 미리 사용자 local 디비에 저장하는 용
    //컬럼으로는 id, s_name,m_ingredient,reason

    @PrimaryKey
    @ColumnInfo(name="id")
    @SerializedName("id")
    var id:Int=0

    @ColumnInfo(name="s_name")
    @SerializedName("s_name")
    var s_ingredient:String=""

    @ColumnInfo(name="m_ingredient")
    @SerializedName("m_ingredient")
    var m_ingredient:String=""

    @ColumnInfo(name="d_reason")
    @SerializedName("d_reason")
    var reason:String=""

    constructor(id:Int,ingredient1:String,ingredient2:String,reason:String) : this() {
        this.id=id
        this.s_ingredient=ingredient1
        this.m_ingredient=ingredient2
        this.reason=reason
    }

    constructor(parcel: Parcel) : this(parcel.readInt()!!,parcel.readString()!!,parcel.readString()!!,parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(s_ingredient)
        parcel.writeString(m_ingredient)
        parcel.writeString(reason)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DynoDurSupplementVO> {
        override fun createFromParcel(parcel: Parcel): DynoDurSupplementVO {
            return DynoDurSupplementVO(parcel)
        }

        override fun newArray(size: Int): Array<DynoDurSupplementVO?> {
            return arrayOfNulls(size)
        }
    }
}
@Dao
interface DurSupplementDAO : RoomDAO<DynoDurSupplementVO>{
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    fun insertDynoDurSupplement(dynoDurSupplementVO: DynoDurSupplementVO):Long

    @Query("SELECT * FROM DurSupplement WHERE m_ingredient=:ingredient")
    fun getDynoDurSupplement(ingredient:String) : List<DynoDurSupplementVO>?

    @Query("SELECT id FROM DurSupplement ORDER BY id DESC LIMIT 1")
    fun getDurID():Int

    @Query("SELECT COUNT(*) FROM DurSupplement")
    fun getLocalDurCount():Int

}