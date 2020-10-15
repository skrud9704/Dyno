package com.example.dyno.View.MyPage.Detail

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.View.MyPage.Detail.Adapters.DetailMAdapter
import com.example.dyno.R
import com.example.dyno.Network.PublicAPI.ApiProc
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.MedicineVO
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.activity_detail_medicine.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DetailMedicineActivity : AppCompatActivity() {
    private lateinit var medicines : MutableList<MedicineVO>
    private val TAG = this::class.java.simpleName
    private lateinit var data : DiseaseVO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_medicine)

        getData()
        setView()
        //dur()

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
        data = intent.getParcelableExtra("DATA")
        //Log.d(TAG,"${data.d_name},${data.d_company}")

        // 등록일자에 값이 없는 경우 = RegistSupplementActivty로부터 데이터를 받아온 경우
        if(data.d_date==""){
            // 1. d_date(등록일자)를 현재 시간으로 셋팅
            // 오레오 이상 SDK 28
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                data.d_date = LocalDate.now().toString()
            }
            //그 이하
            else{
                val today = SimpleDateFormat("YYYY-MM-dd").format(Date())
                data.d_date = today
            }

            // 2. 로컬 DB에 저장(Room)
            insertLocalDB()
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

    private fun dur(){
        val medicineDurApiUrl = "http://apis.data.go.kr/1470000/DURPrdlstInfoService"
        val medicineDurApiKey =
            "aK%2FBiBnzg6KzgMpSBaMjM7G42kbPdMd%2BOk9KPT8NOlGfDW5pRdlKhU2FufcZ4%2FlKFnHBVpi0gbVbfT8FDdTRhg%3D%3D"

        var mString: String = ""//질병별 의약품 이름들 스트링에 붙이기
        val mNum: Int = medicines.size
        for (i in 0 until mNum) {
            mString += medicines[i].name
            mString += "/"
        }
        mString = mString.substring(0, mString.length - 1)
        Log.d("api", "medicines:$mString")

        //test_m.text = data.dName
        m_dur_btn.setOnClickListener {
            val api = apiTask()
            api.execute(medicineDurApiUrl, medicineDurApiKey, mString)
        }
    }

    //공공데이터포털에서 제공하는 Dur 품목 api용
    @SuppressLint("StaticFieldLeak")
    inner class apiTask:AsyncTask<String,String,String>(){
        override fun doInBackground(vararg params: String?): String? {
            val apiProc = ApiProc()
            return apiProc.getInfo(params[0], params[1], params[2])
        }

        override fun onPostExecute(result: String?) {

            Log.d("api", "result:$result")
        }
    }

}