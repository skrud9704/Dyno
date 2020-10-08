package com.example.dyno.ServerAPI

import com.example.dyno.VO.SupplementVO
import retrofit2.Call
import retrofit2.http.*

// 서버를 호출할 메소드를 선언하는 인터페이스
// POST 방식으로 데이터를 주고 받을 때 넘기는 변수는 Field라고 해야한다.
interface RetrofitService {

    // 건강기능식품 이름으로 RDS에서 검색해오기.
    @FormUrlEncoded
    @POST("Supplement/get")
    fun requestSupplement(
        @Field("s_name") s_name: String
    ) : Call<SupplementVO>


}