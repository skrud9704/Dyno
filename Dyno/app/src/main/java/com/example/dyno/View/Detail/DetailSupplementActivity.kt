package com.example.dyno.View.MyPage.Detail

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.R
import com.example.dyno.VO.*
import kotlinx.android.synthetic.main.activity_detail_supplement.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

        supplementService.requestDurSM(s_name).enqueue(object : Callback<ArrayList<DurSMVO>>{
            override fun onFailure(call: Call<ArrayList<DurSMVO>>, t: Throwable) {
                Log.d(TAG,"실패5555 : {$t}")
            }

            override fun onResponse(call: Call<ArrayList<DurSMVO>>, response: Response<ArrayList<DurSMVO>>) {
                Log.d(TAG,"성공^^")

                // 서버에서는 DurVO 중 duritems2와 durReason만 채워서 보냄!
                Log.d(TAG,"병용불가 의약품 주성분 : 총 ${response.body()!!.size}개")
                //Log.d(TAG,"병용불가 이유 : 총 ${response.body()!!.durReason.size}개")

                // Local DB의 의약품들과 비교해서 병용불가 의약품이 있는지 확인
                compareWithUserMedicine(response.body()!!)
            }

        })


    }

    @SuppressLint("SimpleDateFormat")
    private fun compareWithUserMedicine(dur_data : ArrayList<DurSMVO>){
        val localDB = RoomDB.getInstance(this)
        // 사용자의 질병데이터 획득
        val diseaseMinimal : List<DiseaseMinimal> = localDB.diseaseDAO().getDiseaseMinimal()
        val durArrayList : ArrayList<DurVO> = arrayListOf()

        // 1. 서버에서 받아온 데이터를 중복 제거해서 정제 ArrayList(주성분&이유) 형태
        val refinedDurData = removeDuplicateDur(dur_data)

        // 2.사용자가 먹고있는지 확인

        //////////////////////// 4중 for 문 시작 ////////////////////////
        // 제일 바깥 : 질병Loop
        for(disease in diseaseMinimal){
            val duritem1 : ArrayList<String> = arrayListOf()
            val duritem2 : ArrayList<String> = arrayListOf()
            val durReason : ArrayList<String> = arrayListOf()

            // 중간 : 질병에 처방된 의약품Loop
            for(medicine in disease.d_medicines){
                // 콤마 단위로 나누기
                val medicineIngredientArr = medicine.ingredient.split(",")

                // 중간2 : 의약품의 주성분Loop
                ingredientLoop@
                for(ingredient in medicineIngredientArr){
                    // 마지막 : 서버에서 받아온 병용금기 성분Loop
                    for(dur in refinedDurData){

                        // 금기해야할 주성분이 존재한다면!
                        if(ingredient == dur.m_ingredient){
                            // 해당 의약품 추가
                            duritem1.add(medicine.name)
                            // 금기 주성분 추가
                            duritem2.add(dur.m_ingredient)
                            // 이유 추가
                            durReason.add(dur.d_reason)
                            // 중간 (의약품)Loop를 탈출. (refinedData가 중복 데이터를 제거했기때문에 일치하는 즉시 나가도 됨!)
                            break@ingredientLoop
                        }
                    }
                }

            }

            // 일치하는 의약품이 있었다면 여기서 durVO 추가해야함!
            if(duritem1.size!=0){
                // (등록일자)를 현재 시간으로 셋팅
                // 오레오 이상 SDK 28
                val date : String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val now = LocalDateTime.now()
                    now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                }
                //그 이하
                else{
                    val today = SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS").format(Date())
                    today
                }
                durArrayList.add(DurVO(date,2,disease.d_date,disease.d_name,"","",duritem1,duritem2,durReason))
            }
        }
        //////////////////////// 4중 for 문 끝 ////////////////////////

        // 3. db에 넣기!
        for(dur in durArrayList){
            localDB.durDAO().insertDur(dur)
        }

    }

    private fun removeDuplicateDur(dur_data: ArrayList<DurSMVO>) : ArrayList<DurSMVO>{

        ///////////////////////// 중복 제거 시작 /////////////////////////
        /*for(dur in dur_data){
            Log.d(TAG,"받아온 데이터 : ${dur.m_ingredient} - ${dur.d_reason}")
        }*/

        val dur_data2 : ArrayList<DurSMVO> = arrayListOf()
        for(i in 0 until dur_data.size-1){
            for(j in i+1 until dur_data.size){

                // 병용 금기해야할 의약품 주성분 이름이 같으면
                if(dur_data[i].m_ingredient==dur_data[j].m_ingredient){
                    // 고유한 쓰레기 값을 붙여 바꿔버린다!
                    dur_data[j].m_ingredient =dur_data[j].m_ingredient.plus("$j 중복값")
                    //Log.d(TAG,"${i}와 ${j}가 같다!")
                    //Log.d(TAG,"바꾼 이름 : ${dur_data[j].m_ingredient}")

                    // 주성분은 같으나 이유가 서로 다르면 붙인다!
                    if(dur_data[i].d_reason!=dur_data[j].d_reason){
                        dur_data[i].d_reason=dur_data[i].d_reason.plus("\n${dur_data[j].d_reason}")
                        //Log.d(TAG,"이유추가 : ${dur_data[i].d_reason}")
                    }
                }
            }
        }

        // 중복값이라고 표시해둔 것들만 제외하고 dur_data2로 옮김
        for(dur in dur_data){
            if(!dur.m_ingredient.endsWith("중복값"))
                dur_data2.add(dur)
        }

        /*for(dur in dur_date2){
            Log.d(TAG,"주성분 : ${dur.m_ingredient}, 이유 : ${dur.d_reason}")
        }*/

        ///////////////////////// 중복 제거 끝 /////////////////////////

        return dur_data2
    }

}