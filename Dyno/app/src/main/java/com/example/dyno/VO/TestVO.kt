package com.example.dyno.VO

import com.google.gson.annotations.SerializedName

class TestVO {
    @SerializedName("disease_date")
    var disease_date=""
    @SerializedName("m_name")
    var m_name=""
    @SerializedName("s_ingredient")
    var s_ingredient=""
    @SerializedName("m_ingredient")
    var m_ingredient=""
    @SerializedName("dur_reason")
    var dur_reason=""

    constructor(disease_date : String, m_name : String, s_ingredient : String, m_ingredient : String, dur_reason: String){
        this.disease_date = disease_date
        this.m_name = m_name
        this.s_ingredient = s_ingredient
        this.m_ingredient = m_ingredient
        this.dur_reason = dur_reason
    }

    fun printDetail():String{
        return "질병 고유번호(등록날짜) : $disease_date, 의약품 명 : $m_name, 병용불가 의약품 주성분명 : $m_ingredient, 병용불가 건강기능식품 주성분명 : $s_ingredient, 이유 : $dur_reason"
    }

}