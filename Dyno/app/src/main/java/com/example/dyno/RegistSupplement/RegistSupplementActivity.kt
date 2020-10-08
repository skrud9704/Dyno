package com.example.dyno.RegistSupplement

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import com.example.dyno.R
import com.example.dyno.ServerAPI.RetrofitService
import com.example.dyno.ServerAPI.RetrofitClient
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.activity_regist_supplement.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// process
// 0. 이름으로 검색.
// 1. RDS에 해당 건강기능 식품이 존재하는지 검사
// 2. 1.에서 있다면 바로 등록.
// 3. 1.에서 없다면 웹에서 크롤링 후 RDS 저장, 어플에서 등록.
// 일단, RDS 절차 건너뛰고 3부터.

var siteSource : String =""

class RegistSupplementActivity : AppCompatActivity() {

    val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_supplement)

        val retrofit = RetrofitClient.getInstance()
        val supplementService = retrofit.create(RetrofitService::class.java)

        supplementService.requestSupplement("활력").enqueue(object : Callback<SupplementVO>{
            override fun onFailure(call: Call<SupplementVO>, t: Throwable) {
                Log.d(TAG,"실패 : $t")
            }

            override fun onResponse(call: Call<SupplementVO>, response: Response<SupplementVO>) {
                Log.d(TAG,"성공 : ${response.body()!!.m_name}")
            }

        })

        btn_search.setOnClickListener {

        }

    }



}