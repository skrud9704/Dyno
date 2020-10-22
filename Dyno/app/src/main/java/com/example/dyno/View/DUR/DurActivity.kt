package com.example.dyno.View.MyPage.DUR

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.R
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.DurMMTestVO
import com.example.dyno.VO.TestVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class DurActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var retrofit : Retrofit
    private lateinit var durService : RetrofitService
    private val prohibitMedicineList : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dur)


        val compareData = intent.getParcelableExtra<DiseaseVO>("DATA_DISEASE")

        // 서버 연결
        initRetrofit()

        for(medicine in compareData.d_medicines){
            //getDurMM(durService,medicine.name)
            getDurMS(durService,medicine.name)
        }

    }

    private fun initRetrofit(){
        retrofit = RetrofitClient.getInstance()
        durService = retrofit.create(RetrofitService::class.java)
    }

    // 리퀘스트: 의약품 이름, 의-의 병용판단
    private fun getDurMM(service : RetrofitService, m_name : String){
        service.requestDurMM(m_name).enqueue(object : Callback<ArrayList<DurMMTestVO>>{
            override fun onFailure(call: Call<ArrayList<DurMMTestVO>>, t: Throwable) {
                Log.d(TAG,"실패33 : {$t}")
            }

            override fun onResponse(call: Call<ArrayList<DurMMTestVO>>, response: Response<ArrayList<DurMMTestVO>>) {
                Log.d(TAG,"성공^^ 33")
                Log.d(TAG,"받아온 값 사이즈 : ${response.body()!!.size}")
                Log.d(TAG,"전체 값 사이즈 : ${prohibitMedicineList.size}")
            }

        })
    }

    // 리퀘스트: 의약품이름, 의-건 병용판단
    private fun getDurMS(service : RetrofitService, m_name : String){
        val durList : ArrayList<TestVO>  = arrayListOf()
        service.requestDurMS(m_name).enqueue(object : Callback<ArrayList<TestVO>>{
            override fun onFailure(call: Call<ArrayList<TestVO>>, t: Throwable) {
                Log.d(TAG,"실패44 : {$t}")
            }

            override fun onResponse(call: Call<ArrayList<TestVO>>, response: Response<ArrayList<TestVO>>) {
                Log.d(TAG,"성공^^ 44")
                Log.d(TAG,"받아온 값 사이즈 : ${response.body()!!.size}")
                for(data in response.body()!!){
                    Log.d(TAG,data.printDetail())
                }
            }

        })
    }
}