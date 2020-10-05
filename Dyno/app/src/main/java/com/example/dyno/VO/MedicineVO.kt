package com.example.dyno.VO
import android.os.Parcel
import android.os.Parcelable

class MedicineVO() : Parcelable{

    var mCode:String=""//약코드
    var count:Int=0//1회투여횟수
    var amount:Int=0//1회 투약량
    var detail:String=""//세부정보
    var total:Int=0//총투약일수

    constructor(mCode:String,count:Int,amount:Int,detail:String,total:Int):this(){
        this.mCode=mCode
        this.count=count
        this.amount=amount
        this.detail=detail
        this.total=total
    }
    constructor(parcel: Parcel) : this() {
        mCode = parcel.readString()
        count = parcel.readInt()
        amount = parcel.readInt()
        detail = parcel.readString()
        total = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mCode)
        parcel.writeInt(count)
        parcel.writeInt(amount)
        parcel.writeString(detail)
        parcel.writeInt(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MedicineVO> {
        override fun createFromParcel(parcel: Parcel): MedicineVO {
            return MedicineVO(parcel)
        }

        override fun newArray(size: Int): Array<MedicineVO?> {
            return arrayOfNulls(size)
        }
    }
}