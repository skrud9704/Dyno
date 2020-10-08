package com.example.dyno.RegistSupplement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.R
import com.example.dyno.ServerAPI.RetrofitService
import com.example.dyno.ServerAPI.RetrofitClient
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.activity_regist_supplement.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

// process
// 0. 이름으로 검색.
// 1. RDS에 해당 건강기능 식품이 존재하는지 검사
// 2. 1.에서 있다면 바로 등록.
// 3. 1.에서 없다면 웹에서 크롤링 후 RDS 저장, 어플에서 등록.
// 일단, RDS 절차 건너뛰고 3부터.

class RegistSupplementActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName
    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService
    private lateinit var supplementAdapter : SupplementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_supplement)

        // 어댑터 달기
        supplementAdapter = SupplementAdapter(this, arrayListOf())
        search_list.adapter = supplementAdapter
        search_list.layoutManager= LinearLayoutManager(this)

        // 서버 연결
        initRetrofit()

        // 버튼 클릭 시
        btn_search.setOnClickListener {
            // 검색어
            val keyword = input_search.text.toString()
            if(keyword!="")
                connect(supplementService, keyword)
        }

    }

    private fun initRetrofit(){
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun connect(service : RetrofitService, keyword : String){
        service.requestSupplementSimple(keyword).enqueue(object : Callback<ArrayList<SupplementVO>>{
            override fun onFailure(call: Call<ArrayList<SupplementVO>>, t: Throwable) {
                Log.d(TAG,"실패 : {$t}")
            }

            override fun onResponse(
                call: Call<ArrayList<SupplementVO>>,
                response: Response<ArrayList<SupplementVO>>
            ) {
                Log.d(TAG,"성공^^")
                if(response.body()!!.size==0){
                    yes_result.visibility = View.GONE
                    no_result.visibility = View.VISIBLE
                }else {
                    Log.d(TAG,"사이즈 : ${response.body()!!.size}, 첫번째 인자 이름 : ${response.body()!![0].m_name}")
                    supplementAdapter.getData(response.body()!!)
                    yes_result.visibility = View.VISIBLE
                    no_result.visibility = View.GONE
                }
            }
        })
    }



}