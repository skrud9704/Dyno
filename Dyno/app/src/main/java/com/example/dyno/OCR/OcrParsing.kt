package com.example.dyno.OCR

import android.util.Log

class OcrParsing {
    val drugs = "정,전자모기향,캡슐,스트립,주사액,묽은,수성현탁주사액,석회,액,결합형,정제,경화,산,침강,가루," +
            "미결정,비결정성,연고,콜로이드성,시럽,불검화물,좌제,함당,페이스트,함수,레모네이드,합성,활성,과립," +
            "환원형,세립,저치환도,가수,틴달화동결건조물,친수,엑스,멸균,유동,반창고,백신,항독소,틴크,소독용," +
            "정량추출물,치과용,공침물,로코트,착염,데스코트,공중합체,유제,혼합건조겔,수혈용,수지,중쇄,염기성"
    //약들의 어미 종류
    var dArr = drugs.split(",").toTypedArray()
    val resultList : ArrayList<String> = arrayListOf()

    fun prescriptionDrugsR(transText: String): String {
        Log.d("pars_start","ocr 결과"+transText)
        var arr = transText.split("/").toTypedArray()
        //ocr하여 나온 결과값을 띄여쓰기 기준으로 나눔

        var med = ""
        var date:String?=""
        //약으로 추정되는 단어들을 문자열로 만들기
        for (i in arr.indices) {
            for (j in dArr.indices) {
                if (arr[i].contains(dArr[j]) && !arr[i].endsWith(".")) {
                    if(arr[i].endsWith("분")){
                        date += arr[i]
                        date += ","
                    }else{
                        med += arr[i]
                        med += ","
                        resultList.add(arr[i])
                    }
                    break
                }

            }
        }
        //var medArr = med.split(",").toTypedArray()
        //var result=med//+"$"+date
        Log.d("pars_medicine",med)
        return med
    }
    fun prescriptionR(transText: String):String{
        //처방전의 경우 이름으로 검색이 아니라 보험약가코드로 의약품 검색할 거임

        Log.d("pars_start","ocr 결과"+transText)
        val arr = transText.split("/").toTypedArray()
        //ocr하여 나온 결과값을 띄여쓰기 기준으로 나눔
        var dCode=""
        var med=""
        Log.d("pars_start","질병분류기호 있음")
        val reg=Regex("[A-Z]\\d{2}")//질병분류기호를 찾기 위해
        val reg2=Regex("[60][0-9]{8}")//보험약가코드찾기 위해
        val reg3=Regex("[.]")
        val reg4=Regex("^[1]\\d{1}")//I로 시작하는 질병분류기호를 찾기 위해
        val reg5=Regex("[가-힣a-zA-Z]")

        for(i in arr){
            if(reg.containsMatchIn(i)){//질병코드 시작이 I가 아닐때
                Log.d("pars_disease","pars_disease:"+i)
                if(reg3.containsMatchIn(i)){
                    val re=reg3.replace(i,"")
                    dCode+=re
                }else{
                    dCode+=i
                }
                dCode+=","

            }else if(reg4.containsMatchIn(i) && !reg5.containsMatchIn(i)){
                if(i.length in 3..6){
                    dCode+="I"
                    dCode+=i.substring(1,i.length-1)
                    dCode+=","
                }
            }else if(reg2.containsMatchIn(i)){
                var temp=i
                if(temp.length>9){//보험코드만 들어있는게 아니라 앞뒤로 뭔가 다른게 붙어있을 경우
                    temp= reg2.find(i)?.value.toString()//보험약가코드만 추출하여 가져옴
                }
                Log.d("pars_med","pars_med"+temp)
                med+=temp
                med+=","

            }
        }
        Log.d("pars_end","pars_end:"+dCode+"%"+med)
        return dCode+"/"+med
    }
    fun supplementR(trasnText:String){

    }
}