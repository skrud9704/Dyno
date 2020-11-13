package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.example.dyno.LocalDB.RoomDAO
import com.google.gson.annotations.SerializedName

@Entity(tableName="NotRecommend")
class NotRecommendVO() : Parcelable {
    //사용자가 아직 구매하지 않았지만 먹으면 안되는 건강기능식품 원재료가 있을 경우
    //컬럼으로는 d_date,d_name,s_ingredient,reason
    //처방전이름과 처방전 등록일자만 보여줌으로 약마다 이유가 다를 수 있지만 그냥 같이 보여줌

    @PrimaryKey
    @ColumnInfo(name="d_date")
    @SerializedName("d_date")
    var d_date:String=""

    @ColumnInfo(name="d_name")
    @SerializedName("d_name")
    var d_name:String=""

    @ColumnInfo(name="s_ingredient")
    @SerializedName("s_ingredient")
    var s_ingredient:String=""

    @ColumnInfo(name="reason")
    @SerializedName("reason")
    var reason:String=""

    constructor(date:String,name:String,ingredient:String,reason:String) : this() {
        this.d_date=date
        this.d_name=name
        this.s_ingredient=ingredient
        this.reason=reason
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!,parcel.readString()!!,parcel.readString()!!,
        parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(d_date)
        parcel.writeString(d_name)
        parcel.writeString(s_ingredient)
        parcel.writeString(reason)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotRecommendVO> {
        override fun createFromParcel(parcel: Parcel): NotRecommendVO {
            return NotRecommendVO(parcel)
        }

        override fun newArray(size: Int): Array<NotRecommendVO?> {
            return arrayOfNulls(size)
        }
    }
}

@Dao
interface NotRecommmendDAO : RoomDAO<NotRecommendVO>{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSupplement(notRecommendVO: NotRecommendVO): Long

    @Query("SELECT COUNT(DISTINCT s_ingredient) FROM NotRecommend")
    fun getDurItemCount():Int

    @Query("SELECT * FROM NotRecommend WHERE s_ingredient=:ingredient")
    fun getNotRecommend(ingredient: String):NotRecommendVO
}