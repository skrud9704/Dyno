package com.example.dyno.RegistMedicine

import android.util.Log

class ocrParsing {
    val drugs = "정,전자모기향,캡슐,스트립,주사액,묽은,수성현탁주사액,석회,액,결합형,정제,경화,산,침강,가루," +
            "미결정,비결정성,연고,콜로이드성,시럽,불검화물,좌제,함당,페이스트,함수,레모네이드,합성,활성,과립," +
            "환원형,세립,저치환도,가수,틴달화동결건조물,친수,엑스,멸균,유동,반창고,백신,항독소,틴크,소독용," +
            "정량추출물,치과용,공침물,로코트,착염,데스코트,공중합체,유제,혼합건조겔,수혈용,수지,중쇄,염기성"
    //약들의 어미 종류
    var dArr = drugs.split(",").toTypedArray()
    fun prescriptionDrugsR(transText: String): String? {
        Log.d("pars_start",transText)
        var arr = transText.split(" ").toTypedArray()
        //ocr하여 나온 결과값을 띄여쓰기 기준으로 나눔

        var med: String? = ""
        var date:String?=""
        //약으로 추정되는 단어들을 문자열로 만들기
        for (i in arr.indices) {
            for (j in dArr.indices) {
                if (arr[i].endsWith(dArr[j]) && !arr[i].endsWith(".")) {
                    if(arr[i].endsWith("분")){
                        date += arr[i]
                        date += ","
                    }else{
                        med += arr[i]
                        med += ","
                    }
                    break
                }

            }
        }
        var medArr = med?.split(",")?.toTypedArray()
        var result=med//+"$"+date
        Log.d("pars_medicine",result)
        return result
    }
    fun prescriptionR(transText: String){

    }
    fun supplementR(trasnText:String){

    }
}