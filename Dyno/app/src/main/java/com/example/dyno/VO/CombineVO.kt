package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable

class CombineVO() : Parcelable{
    var lookFor: String =""//api에 보낼 약 이름
    var result: String =""//같이 먹으면 안되는 거 이름
    var reason: String=""  //이유
    var item:MutableList<CombineVO> = mutableListOf()

   constructor(lookFor:String,item:ArrayList<CombineVO>):this(){
       this.lookFor=lookFor
       this.item=item
   }
    constructor(result:String,reason:String):this(){
        this.result=result
        this.reason=reason
    }

    constructor(parcel: Parcel) : this() {
        /*c1 = parcel.readString()
        c2 = parcel.readString()
        result = parcel.readInt()*/

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
       /* parcel.writeString(c1)
        parcel.writeString(c2)
        parcel.writeInt(result)*/
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