package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable

class CombineVO() : Parcelable{
    var c1: String =""
    var c2: String =""
    var result: Int =-1     //1 : 정상, 2 : 조심, 3 : 경고

    constructor(c1 : String, c2: String, result : Int) : this(){
        this.c1 = c1
        this.c2 = c2
        this.result = result
    }

    constructor(parcel: Parcel) : this() {
        c1 = parcel.readString()
        c2 = parcel.readString()
        result = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(c1)
        parcel.writeString(c2)
        parcel.writeInt(result)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CombineVO> {
        override fun createFromParcel(parcel: Parcel): CombineVO {
            return CombineVO(parcel)
        }

        override fun newArray(size: Int): Array<CombineVO?> {
            return arrayOfNulls(size)
        }
    }
}