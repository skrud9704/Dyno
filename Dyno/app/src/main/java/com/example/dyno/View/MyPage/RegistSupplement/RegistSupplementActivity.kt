package com.example.dyno.RegistSupplement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.OCR.CameraActivity
import com.example.dyno.Detail.DetailSupplementActivity
import com.example.dyno.R
import com.example.dyno.Network.RetrofitService
import com.example.dyno.Network.RetrofitClient
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
        // 어댑터에 커스텀리스너(SupplementClickListener) 달기
        supplementAdapter.setSupplementClickListener(object : SupplementAdapter.SupplementClickListener{
            override fun onItemClick(position: Int) {
                // 어댑터 뷰홀더에서 선택한 데이터 정보 가져옴.
                val clickedItem = supplementAdapter.getData(position)
                //Toast.makeText(applicationContext,clickedItem.m_name,Toast.LENGTH_SHORT).show()

                // 서버로부터 선택된 건강기능식품의 전체정보를 가져오고, (어댑터 리스트는 간략한 정보만 가졌음)
                // 건강기능식품 디테일 액티비티로 이동 (service가 enqueue(비동기)여서 그쪽에서 이동해줘야함.)
                getSelectedSingle(supplementService,clickedItem.m_name)

            }

        })
        search_list.adapter = supplementAdapter
        search_list.layoutManager= LinearLayoutManager(this)

        // 서버 연결
        initRetrofit()

        // 검색 버튼 클릭 시
        btn_search.setOnClickListener {
            // 검색어
            val keyword = input_search.text.toString()
            // 연결
            if(keyword!="")
                getSearchList(supplementService, keyword)
        }

        // 성분 직접 찍기 클릭 시
        camera_btn.setOnClickListener {
            val intent = Intent(this,CameraActivity::class.java)
            intent.putExtra("DATA","supplement")
            startActivity(intent)
        }

    }

    private fun initRetrofit(){
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun getSearchList(service : RetrofitService, keyword : String){
        // 키워드로 검색
        service.requestList(keyword).enqueue(object : Callback<ArrayList<SupplementVO>>{
            override fun onFailure(call: Call<ArrayList<SupplementVO>>, t: Throwable) {
                Log.d(TAG,"실패 : {$t}")
            }

            override fun onResponse(call: Call<ArrayList<SupplementVO>>, response: Response<ArrayList<SupplementVO>>) {
                Log.d(TAG,"성공^^")
                // 결과가 없을 경우
                if(response.body()!!.size==0){
                    yes_result.visibility = View.GONE           // 결과 있음 GONE
                    no_result.visibility = View.VISIBLE         // 결과 없음 VISIBLE
                }
                // 결과가 있을 경우
                else {
                    //Log.d(TAG,"사이즈 : ${response.body()!!.size}, 첫번째 인자 이름 : ${response.body()!![0].m_name}")
                    supplementAdapter.setNewData(response.body()!!)    // 어댑터에 데이터 업데이트
                    yes_result.visibility = View.VISIBLE            // 결과 있음 VISIBLE
                    no_result.visibility = View.GONE                // 결과 없음 GONE
                }
            }
        })
    }

    private fun getSelectedSingle(service : RetrofitService, keyword : String) {

        service.requestSingle(keyword).enqueue(object : Callback<SupplementVO>{
            override fun onFailure(call: Call<SupplementVO>, t: Throwable) {
                Log.d(TAG,"실패 : {$t}")
            }

            override fun onResponse(call: Call<SupplementVO>, response: Response<SupplementVO>) {
                Log.d(TAG,"성공^^ 22")
                Log.d(TAG,"${response.body()!!.m_name},${response.body()!!.m_company}")
                // 현재 액티비티 종료하고, 건강기능식품 디테일 액티비티로 넘어감.
                val intent = Intent(applicationContext,DetailSupplementActivity::class.java)
                intent.putExtra("DATA2",response.body()!!)
                startActivity(intent)
                finish()
            }

        })

    }


}