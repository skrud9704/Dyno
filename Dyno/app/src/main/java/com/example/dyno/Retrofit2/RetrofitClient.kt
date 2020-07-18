package com.example.dyno.Retrofit2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object{
        private val retrofitClient: RetrofitClient= RetrofitClient()

        fun getInstance(): RetrofitClient{
            return retrofitClient
        }
    }

    fun buildRetrofit(): RetrofitService {
        // Retrofit 객체 생성하는 부분
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl("http://203.252.166.148:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Retrofit 객체를 통해 인터페이스 생성성
        val service: RetrofitService = retrofit!!.create(RetrofitService::class.java)
        return service
    }
}