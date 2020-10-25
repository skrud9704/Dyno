package com.example.dyno.View.MyPage.Detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.View.MyPage.Detail.Adapters.DetailMAdapter
import com.example.dyno.R
import com.example.dyno.VO.*
import com.example.dyno.View.MyPage.DUR.DurActivity
import kotlinx.android.synthetic.main.activity_detail_medicine.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DetailMedicineActivity : AppCompatActivity() {
    private lateinit var medicines : MutableList<MedicineVO>
    private val TAG = this::class.java.simpleName
    private lateinit var data : DiseaseVO
    private var  durItems:ArrayList<DurMMTestVO> = ArrayList()
    private lateinit var  userDurItem:ArrayList<DurVO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_medicine)

        getData()
        setView()

        m_dur_btn.setOnClickListener {
            val intent = Intent(this,DurActivity::class.java)
            intent.putExtra("DATA_DISEASE",data)
            startActivity(intent)
        }

    }

    // 복용 마지막 날짜를 계산하는 함수
    /*fun calculateEdate(medicines : ArrayList<MedicineVO>, sDate : String) : String {
        // date format은 YYYY-MM-dd
        // medicines 중 투약일 수(total)가 제일 긴 것 = maxPeriod
        // sDate + maxPeriod
        var maxPeriod = -1
        for(medicineModel in medicines){
            if(maxPeriod < medicineModel.total)
                maxPeriod = medicineModel.total
        }
        // sDate에 더하기를 할 수 있게 LocalDate로 바꾸는 작업.
        val localDate =
            // API Level 26이상부터 가능.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.parse(sDate, DateTimeFormatter.ISO_DATE)      // ISO_DATE : YYYY-mm-dd
            }
            // 보류.
            else {
                TODO("VERSION.SDK_INT < O")
            }

        // 더하기 후 리턴.
        val addedDate = localDate.plusDays(maxPeriod.toLong())
        return addedDate.toString()
    }*/

    @SuppressLint("SimpleDateFormat")
    private fun getData(){

        /* From RegistMedicineActivity / MyPageActivity(MedicineAdapter) */
        data = intent.getParcelableExtra("DATA_DISEASE")
        //Log.d(TAG,"${data.d_name},${data.d_company}")

        // 등록일자에 값이 없는 경우 = RegistSupplementActivty로부터 데이터를 받아온 경우
        if(data.d_date==""){
            // 1. d_date(등록일자)를 현재 시간으로 셋팅
            // 오레오 이상 SDK 28
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val now = LocalDateTime.now()
                data.d_date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }
            //그 이하
            else{
                val today = SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Date())
                data.d_date = today
            }

            // 2. 로컬 DB에 저장(Room)
            insertLocalDB()
            var medicine=""
            medicines = data.d_medicines
            for(item in medicines){
                medicine+=item.name
                medicine+=","
            }
            getDurInfo(medicine)
        }
    }

    private fun insertLocalDB(){
        Log.d(TAG,"RoomDB 접근")
        // DB 싱글톤으로 생성.
        val localDB = RoomDB.getInstance(this)
        localDB.diseaseDAO().insertDisease(data)
        Toast.makeText(this,"나의 의약품에 추가했습니다.", Toast.LENGTH_SHORT).show()

        // DB 닫기.
        RoomDB.destroyInstance()
    }

    private fun setView(){
        //UI
        detail_m_name.text = data.d_name                                             // 질병 명
        detail_m_sdate.text = data.d_date                                             // 등록날짜(복용시작날짜)
        //detail_m_edate.text = calculateEdate(data.medicines,data.date)              // 등록날짜 + 의약품 중 제일 긴 복용기간 = 복용끝날짜
        recycler_detail_m.adapter = DetailMAdapter(this,data.d_medicines)     // RecyclerView Adapter
        recycler_detail_m.layoutManager = LinearLayoutManager(this)         // 이거 해줘야 레이아웃 보임.
    }


    private fun getDurInfo(medicine: String) {
        val retrofit =RetrofitClient.getInstance()
        val durService=retrofit.create(RetrofitService::class.java)

        val localDB = RoomDB.getInstance(this)
        Log.d(TAG, "dur서버로 보내는 데이터 : $medicine")

        durService.requestDurMM(medicine).enqueue(object:Callback<ArrayList<DurMMTestVO>>{
            override fun onFailure(call: Call<ArrayList<DurMMTestVO>>, t: Throwable) {
                Log.d(TAG, "실패 {$t}")
            }

            @SuppressLint("SimpleDateFormat")
            override fun onResponse(call: Call<ArrayList<DurMMTestVO>>, response: Response<ArrayList<DurMMTestVO>>) {
                val durNames:ArrayList<String> = ArrayList()
                for(dur in response.body()!!){
                    val temp:String=dur.durName
                    durNames.add(temp)
                    durItems.add(dur)
                }

                if(durItems.size==0){
                    //Log.d(TAG,"dur 없음")
                }else{
                    val diseaseList:List<DiseaseMinimal> = localDB.diseaseDAO().getDiseaseMinimal()
                    for(item in diseaseList){
                        val durMMList1:ArrayList<String> = ArrayList()//item1에 해당하는 의약품 리스트
                        val durMMList2:ArrayList<String> = ArrayList()//items1에 해당하는 의약품 리스트
                        val durReason:ArrayList<String> = ArrayList()//dur 이유
                        //Log.d(TAG,item.d_date)//기존에 저장되어 있는 아이들 키값
                        for(med in item.d_medicines){//처방전에 있는 의약품마다
                            val check:Boolean = durNames.contains(med.name)
                            if(check){
                                val temp = durNames.indexOf(med.name)
                                val durMM:DurMMTestVO=durItems[temp]
                                durMMList1.add(durMM.mName)//새로등록하는 아이
                                durMMList2.add(med.name)//기존에 있던 아이
                                durReason.add(durMM.durReason)//같이 먹으면 안되는 이유
                            }
                        }
                        if(durMMList1.size!=0){
                            var today = ""
                            today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val now = LocalDateTime.now()
                                now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS"))
                            } else{
                                SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS").format(Date())
                            }

                            val durVo = DurVO(today,1,data.d_date,data.d_name,item.d_date,item.d_name,
                            durMMList1,durMMList2,durReason)
                            //Log.d(TAG,"병용 room insert:"+item.d_name+today)
                            localDB.durDAO().insertDur(durVo)
                        }
                    }
                    //RoomDB.destroyInstance()
                }
            }
        })

        durService.requestDurMS(medicine).enqueue(object : Callback<ArrayList<DurMSVO>>{
            override fun onFailure(call: Call<ArrayList<DurMSVO>>, t: Throwable) {
                Log.d(TAG, "실패66666 {$t}")
            }

            override fun onResponse(call: Call<ArrayList<DurMSVO>>, response: Response<ArrayList<DurMSVO>>) {
                Log.d(TAG, "성공 ^^66666")
                Log.d(TAG, "총 : ${response.body()!!.size}")

                // 사용자가 먹고 있는 건강기능식품과 데이터 비교
                compareWithUserSupplement(response.body()!!)

            }

        })

    }

    @SuppressLint("SimpleDateFormat")
    private fun compareWithUserSupplement(dur_data : ArrayList<DurMSVO>){
        val localDB = RoomDB.getInstance(this)

        // 로컬으로부터 사용자가 복용중인 건강기능식품 정보 획득
        val supplementMinimal : List<SupplementMinimal> = localDB.supplementDAO().getSupplementMinimal()

        val durArrayList : ArrayList<DurVO> = arrayListOf()
        val duritem1 : ArrayList<String> = arrayListOf()
        val duritem2 : ArrayList<String> = arrayListOf()
        val durReason : ArrayList<String> = arrayListOf()

        // 제일 바깥 : 건강기능식품 Loop
        for(supplement in supplementMinimal){
            Log.d(TAG,"복용 중인 건강기능식품 : $supplement")
            val sIngredientArr = supplement.s_ingredient.split(",")

            // 중간 : 건강기능식품의 기능성원재료 Loop
            for(ingredient in sIngredientArr){
                Log.d(TAG,"건강기능식품의 기능성원재료 : $ingredient")
                // 마지막 : 서버에서 가져온 병용금기 성분 Loop
                for(dur in dur_data){
                    Log.d(TAG,"비교하려는 기능성원재료 : ${dur.s_ingredient}")
                    // 건강기능식품의 기능성원재료가 해당 금기원재료를 포함하고 있다면
                    if(ingredient.contains(dur.s_ingredient)){
                        Log.d(TAG,"포함하고 있따!")
                        // 의약품 이름 추가
                        duritem1.add(dur.m_name)
                        // 건강기능식품의 기능성원재료 추가
                        duritem2.add(dur.s_ingredient)
                        // 병용 금기 이유 추가
                        durReason.add(dur.d_reason)
                    }

                }
            }

            // 여기서 durArrayList 추가해야함
            if(duritem1.size!=0){
                Log.d(TAG,"추가한다!")
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
                durArrayList.add(DurVO(date,2,data.d_date,data.d_name,"","",duritem1,duritem2,durReason))
            }
        }

        // 3. db에 넣기!
        for(dur in durArrayList){
            localDB.durDAO().insertDur(dur)
        }


    }
}

