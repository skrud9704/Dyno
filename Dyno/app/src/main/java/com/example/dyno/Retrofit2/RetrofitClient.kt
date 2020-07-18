package com.example.dyno.Retrofit2

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object{
        private val retrofitClient: RetrofitClient= RetrofitClient()

        fun getInstance(): RetrofitClient{
            return retrofitClient
        }
    }

    fun buildRetrofit(): RetrofitService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level=HttpLoggingInterceptor.Level.BODY


        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(httpLoggingInterceptor)
            connectTimeout(15,TimeUnit.SECONDS)
            writeTimeout(15,TimeUnit.SECONDS)
            readTimeout(15,TimeUnit.SECONDS)
        }.build()

        // Retrofit 객체 생성하는 부분
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl("https://203.252.166.148:443/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Retrofit 객체를 통해 인터페이스 생성성
        val service: RetrofitService = retrofit!!.create(RetrofitService::class.java)
        return service
    }
}