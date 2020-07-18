package com.example.dyno.Retrofit2

import com.example.dyno.VO.UserVO
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("30")
    fun read(@Path("no") no: String)
        : Call<JsonObject>

    @FormUrlEncoded
    @POST("index.php")
    fun requestJoin(
        @Field("userId") userId:String
    ) : Call<String>
}