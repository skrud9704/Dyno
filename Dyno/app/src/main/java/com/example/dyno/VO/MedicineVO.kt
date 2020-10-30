package com.example.dyno.VO
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MedicineVO() : Parcelable{

    @SerializedName("m_name")
    var name:String=""                      // 약이름

    @SerializedName("m_effect_code")
    var effect_code:Int = -1                // 약효분류군

    @SerializedName("m_ins_code")
    var ins_code :String=""                    // 보험약가코드

    @SerializedName("m_effect")
    var effect:String=""                    // 효능 효과

    @SerializedName("m_dosage")
    var dosage :String=""                   // 용법 용량

    @SerializedName("m_ingredient")
    var ingredient :String=""               // 주성분

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        effect_code = parcel.readInt()
        ins_code = parcel.readString()
        effect = parcel.readString()
        dosage = parcel.readString()
        ingredient = parcel.readString()
    }


    constructor(name:String,effect_code:Int, ins_code : String, effect:String, dosage:String,ingredient:String):this(){
        this.name=name
        this.effect_code=effect_code
        this.ins_code=ins_code
        this.effect=effect
        this.dosage=dosage
        this.ingredient=ingredient
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(effect_code)
        parcel.writeString(ins_code)
        parcel.writeString(effect)
        parcel.writeString(dosage)
        parcel.writeString(ingredient)
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


    fun getIngredientsArray() : List<String>{
        return ingredient.split(",")
    }

}