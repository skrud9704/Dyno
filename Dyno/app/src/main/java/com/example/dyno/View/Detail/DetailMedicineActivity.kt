package com.example.dyno.View.Detail

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.View.Detail.Adapters.DetailMAdapter
import com.example.dyno.R
import com.example.dyno.VO.*
import kotlinx.android.synthetic.main.activity_detail_medicine.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class DetailMedicineActivity : AppCompatActivity() {
    private lateinit var medicines : MutableList<MedicineVO>
    private val TAG = this::class.java.simpleName
    private lateinit var data : DiseaseVO
    private var  durItems:ArrayList<DurMMTestVO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_medicine)

        getData()
        setView()

    }

    @SuppressLint("SimpleDateFormat")
    private fun getData(){
        // 의약품 등록, 혹은 마이페이지에서 선택해서 들어오는 경우
        // From RegistMedicineActivity / MyPageActivity(MedicineAdapter)
        if(intent.hasExtra("DATA_DISEASE")){
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

        // 메인화면에서 선택해 들어오는 경우
        if(intent.hasExtra("DISEASE_FROM_MAIN")){
            val key : String = intent.getStringExtra("DISEASE_FROM_MAIN")
            val localDB = RoomDB.getInstance(this)
            data = localDB.diseaseDAO().getDisease(key)

            // DB 닫기.
            RoomDB.destroyInstance()
        }

    }

    private fun insertLocalDB(){
        Log.d(TAG,"RoomDB 접근")
        // DB 싱글톤으로 생성.
        val localDB = RoomDB.getInstance(this)
        localDB.diseaseDAO().insertDisease(data)
        Toast.makeText(this,"나의 의약품에 추가했습니다.", Toast.LENGTH_SHORT).show()

        // 병용 주의 건강기능식품 성분 뽑기.
        val mList : ArrayList<MedicineVO> = data.d_medicines
        val sIngredient : HashSet<String> = HashSet()
        for(medicine in mList){
            val ingredients = medicine.ingredient.split(",")
            for(ingredient in ingredients)
                sIngredient.add(ingredient)
        }

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

            override fun onResponse(call: Call<ArrayList<DurMMTestVO>>, response: Response<ArrayList<DurMMTestVO>>) {
                Log.d(TAG,"성공 의의 : ${response.body()!!.size}")
                compareWithUserMedicine(response.body()!!)
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
                durArrayList.add(DurVO(date,2,data.d_date,data.d_name,"",supplement.s_name,duritem1,duritem2,durReason))
            }
        }

        // 3. db에 넣기!
        for(dur in durArrayList){
            localDB.durDAO().insertDur(dur)
        }


    }

    @SuppressLint("SimpleDateFormat")
    private fun compareWithUserMedicine(durMMdata:ArrayList<DurMMTestVO>){
        val localDB = RoomDB.getInstance(this)
        //사용자가 현재 복용중인 처방전 list 가져오기
        val diseaseList:List<DiseaseMinimal> = localDB.diseaseDAO().getDiseaseMinimal()

        for(dur in durMMdata){//먹으면 안되는 금기 성분 개수 만큼
            Log.d(TAG,"의의 병용금기 판단시작"+dur.durName)
            for(item in diseaseList){//처방전 마다다
                Log.d(TAG,"의의 병용금기 판단시작 처방전"+item.d_date)
                val durMMList1:ArrayList<String> = ArrayList()//item1에 해당하는 의약품 리스트
                val durMMList2:ArrayList<String> = ArrayList()//items1에 해당하는 의약품 리스트
                val durReason:ArrayList<String> = ArrayList()//dur 이유
                for(med in item.d_medicines){//의약품 마다
                    Log.d(TAG,"의의 병용금기 판단시작 의약품"+med.name)
                    if(dur.durName==med.ingredient){//내가 가지고 있는 의약품의 주성분과 병용금기 성분이 같을 경우
                        durMMList1.add(dur.mName)//새로등록하는 아이
                        durMMList2.add(med.name)//기존에 있던 아이
                        durReason.add(dur.durReason)//같이 먹으면 안되는 이유
                        Log.d(TAG,"의의 병용금기 존재"+dur.mName+dur.durName)
                    }
                }
                if(durMMList1.size!=0){
                    val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val now = LocalDateTime.now()
                        now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS"))
                    } else{
                        SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS").format(Date())
                    }
                    val durVo = DurVO(today,1,data.d_date,data.d_name,item.d_date,item.d_name,
                        durMMList1,durMMList2,durReason)
                    Log.d(TAG,"의의 병용 room insert:"+item.d_name+today)
                    localDB.durDAO().insertDur(durVo)
                }
           }

        }

    }
}

