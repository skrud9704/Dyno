package com.example.dyno.VO

import com.google.gson.annotations.SerializedName

class DiseaseGuessVO {
    @SerializedName("medicine_list")
    var medicineList : ArrayList<MedicineVO> = arrayListOf()    // OCR 파싱된 결과로부터 RDS에서 값 찾아온 의약품들

    @SerializedName("disease_rank_name")
    var diseaseNameList : ArrayList<String> = arrayListOf()     // 찾아온 의약품들에서 추측된 질병 이름들

    @SerializedName("disease_rank_code")
    var diseaseCodeList : ArrayList<String> = arrayListOf()     // 찾아온 의약품들에서 추측된 질병 이름들

    @SerializedName("disease_rank_per")
    var diseasePerList : ArrayList<String> = arrayListOf()      // 찾아온 의약품들에서 추측된 질병 퍼센트

    @SerializedName("disease_type")
    var type : Int = -1                                         // 처방전 2, 약봉투 1
}