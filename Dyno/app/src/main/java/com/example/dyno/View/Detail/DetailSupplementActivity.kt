package com.example.dyno.View.MyPage.Detail

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.View.MyPage.Detail.Adapters.DetailSAdapter
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO
import com.example.dyno.VO.TestVO
import kotlinx.android.synthetic.main.activity_detail_supplement.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class DetailSupplementActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var data : SupplementVO

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_supplement)

        // intent 받아온 데이터 저장
        getData()
        // 데이터 바탕으로 View 셋팅
        setView()

    }

    @SuppressLint("SimpleDateFormat")
    private fun getData(){
        /* From RegistSupplementActivity / MyPageActivity(SupplementAdapter) */
        data = intent.getParcelableExtra("DATA2")
        //Log.d(TAG,"${data.m_name},${data.m_company}")

        // 등록일자에 값이 없는 경우 = RegistSupplementActivty로부터 데이터를 받아온 경우 = 처음으로 등록하는 경우
        if(data.m_date==""){
            // 1. m_date(등록일자)를 현재 시간으로 셋팅
            // 오레오 이상 SDK 28
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                data.m_date = LocalDate.now().toString()
            }
            //그 이하
            else{
                val today = SimpleDateFormat("YYYY-MM-dd").format(Date())
                data.m_date = today
            }

            // 2. 로컬 DB에 저장(Room)
            insertLocalDB()

            // 3. 병용판단 시작
            durSM(data.m_name)
        }
    }

    private fun setView(){
        detail_s_name.text = data.m_name
        detail_s_date.text = data.m_date
        detail_s_ingredient.text = data.m_ingredients
        detail_s_info.text = data.m_ingredients_info

        //val detailSAdapter = DetailSAdapter(this,data.m_ingredients,data.m_ingredients_info)
        //recycler_detail_s.adapter = detailSAdapter
        //recycler_detail_s.layoutManager=LinearLayoutManager(this)
    }

    private fun insertLocalDB(){
        Log.d(TAG,"RoomDB 접근")
        // DB 싱글톤으로 생성.
        val localDB = RoomDB.getInstance(this)
        localDB.supplementDAO().insertSupplement(data)
        Toast.makeText(this,"나의 건강기능식품에 추가했습니다.",Toast.LENGTH_SHORT).show()

        // DB 닫기.
        RoomDB.destroyInstance()
    }

    // 병용판단 건강기능식품-의약품
    private fun durSM(s_name : String){
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
        Log.d(TAG,"보내는 데이터 $s_name")

        supplementService.requestDurSM(s_name).enqueue(object : Callback<ArrayList<TestVO>>{
            override fun onFailure(call: Call<ArrayList<TestVO>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<ArrayList<TestVO>>, response: Response<ArrayList<TestVO>>) {
                TODO("Not yet implemented")
            }

        })


    }

}