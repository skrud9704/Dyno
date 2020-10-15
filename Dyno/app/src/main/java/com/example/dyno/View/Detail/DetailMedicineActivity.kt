package com.example.dyno.View.MyPage.Detail

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.View.MyPage.Detail.Adapters.DetailMAdapter
import com.example.dyno.R
import com.example.dyno.Network.PublicAPI.ApiProc
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.MedicineVO
import kotlinx.android.synthetic.main.activity_detail_medicine.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailMedicineActivity : AppCompatActivity() {
    private lateinit var medicines : MutableList<MedicineVO>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_medicine)

        //Intent From MyPage>adapter>MedicineAdapter
        // 질병 데이터 : 질병명,등록날짜,질병코드,의약품객체배열.
        val data = intent.getParcelableExtra<DiseaseVO>("DATA")

        medicines = data.medicines
        Log.d("api", "medicines:$medicines")
        //UI
        detail_m_name.text = data.dName                                             // 질병 명
        detail_m_sdate.text = data.date                                             // 등록날짜(복용시작날짜)
        //detail_m_edate.text = calculateEdate(data.medicines,data.date)              // 등록날짜 + 의약품 중 제일 긴 복용기간 = 복용끝날짜
        recycler_detail_m.adapter = DetailMAdapter(this,data.medicines)     // RecyclerView Adapter
        recycler_detail_m.layoutManager = LinearLayoutManager(this)         // 이거 해줘야 레이아웃 보임.

        dur()

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