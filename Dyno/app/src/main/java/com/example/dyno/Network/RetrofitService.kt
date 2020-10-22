package com.example.dyno.Network

import com.example.dyno.VO.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

// 서버를 호출할 메소드를 선언하는 인터페이스
// POST 방식으로 데이터를 주고 받을 때 넘기는 변수는 Field라고 해야한다.
interface RetrofitService {

    @FormUrlEncoded
    @POST("Supplement/getList")
    fun requestSupplementList(
        @Field("s_name") s_name: String
    ) : Call<ArrayList<SupplementVO>>

    @FormUrlEncoded
    @POST("Supplement/getSingle")
    fun requestSupplementSingle(
        @Field("s_name") s_name: String
    ) : Call<SupplementVO>

    @FormUrlEncoded
    @POST("Test/getList")
    fun requestMedicineList(
        @Field("m_names") m_names: String
    ) : Call<ArrayList<MedicineVO>>

    @FormUrlEncoded
    @POST("Medicine/medicinesToDisease")
    fun requestMedicineAndDiseaseList(
        @Field("m_names") m_names: String
    ) : Call<DiseaseGuessVO>

    @FormUrlEncoded
    @POST("Medicine/medicinesToDisease")
    fun requestDiseaseList(
        @Field("m_name") m_name: String
    ) : Call<ArrayList<DiseaseGuessVO>>

    @FormUrlEncoded
    @POST("Dur/getMM")
    fun requestDurMM(
        @Field("m_name") m_name: String
    ) : Call<ArrayList<DurMMTestVO>>

    @FormUrlEncoded
    @POST("Dur/getMS")
    fun requestDurMS(
        @Field("m_name") m_name: String
    ) : Call<ArrayList<TestVO>>

}