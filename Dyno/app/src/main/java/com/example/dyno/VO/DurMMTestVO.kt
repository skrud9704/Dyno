package com.example.dyno.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class DurMMTestVO() : Parcelable {
    @SerializedName("disease_date")
    var dDate:String=""

    @SerializedName("m_name")
    var mName:String=""

    @SerializedName("dur_name")
    var durName:String=""

    @SerializedName("dur_ingredient")
    var ingredient:String=""

    @SerializedName("dur_reason")
    var durReason:String=""

    constructor(parcel: Parcel) : this() {

        mName=parcel.readString()
        durName=parcel.readString()
        ingredient=parcel.readString()
        durReason=parcel.readString()
    }

    constructor(date:String,name:String,durName:String,ingredient:String,reason:String):this(){
        this.dDate=date
        this.mName=name
        this.durName=durName
        this.ingredient=ingredient
        this.durReason=reason
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dDate)
        parcel.writeString(mName)
        parcel.writeString(durName)
        parcel.writeString(ingredient)
        parcel.writeString(durReason)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DurMMTestVO> {
        override fun createFromParcel(parcel: Parcel): DurMMTestVO {
            return DurMMTestVO(parcel)
        }

        override fun newArray(size: Int): Array<DurMMTestVO?> {
            return arrayOfNulls(size)
        }
    }
    fun printDetail():String{
        return "의약품 : $mName, dur의약품 : $durName, 이유 : $durReason"
    }

}