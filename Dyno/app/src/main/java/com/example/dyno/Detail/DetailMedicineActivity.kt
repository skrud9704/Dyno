package com.example.dyno.Detail

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.Detail.Adapters.DetailMAdapter
import com.example.dyno.R
import com.example.dyno.Server.ApiProc
import com.example.dyno.Server.ServerProc
import com.example.dyno.VO.CombineVO
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.DurVO
import com.example.dyno.VO.MedicineVO
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_medicine.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailMedicineActivity : AppCompatActivity() {
    private lateinit var mLocalDatabase : FirebaseDatabase
    private lateinit var mRef : DatabaseReference
    var TABLE_NAME = "DUR"
    var SHARED_PREF = "SaveLogin"
    var SHARED_PREF_DID = "did"
    var durList=mutableListOf<DurVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_medicine)

        //Intent From MyPage>adapter>MedicineAdapter
        // 질병 데이터 : 질병명,등록날짜,질병코드,의약품객체배열.
        val data = intent.getParcelableExtra<DiseaseVO>("DATA")

        var medicines: MutableList<MedicineVO> = data.medicines
        Log.d("api", "medicines:" + medicines)
        //UI
        detail_m_name.text = data.dName                                             // 질병 명
        detail_m_sdate.text = data.date                                             // 등록날짜(복용시작날짜)
        detail_m_edate.text =
            calculateEdate(data.medicines, data.date)              // 등록날짜 + 의약품 중 제일 긴 복용기간 = 복용끝날짜
        recycler_detail_m.adapter = DetailMAdapter(this, data.medicines)     // RecyclerView Adapter
        recycler_detail_m.layoutManager = LinearLayoutManager(this)         // 이거 해줘야 레이아웃 보임.

        val medicineDurApiUrl = "http://apis.data.go.kr/1470000/DURPrdlstInfoService"
        val medicineDurApiKey =
            "aK%2FBiBnzg6KzgMpSBaMjM7G42kbPdMd%2BOk9KPT8NOlGfDW5pRdlKhU2FufcZ4%2FlKFnHBVpi0gbVbfT8FDdTRhg%3D%3D"

        var mString: String = ""//질병별 의약품 이름들 스트링에 붙이기
        var mNum: Int = medicines.size
        for (i in 0 until mNum) {
            mString += medicines[i].mName
            mString += "/"
        }
        mString = mString.substring(0, mString.length - 1)
        Log.d("api", "medicines:" + mString)

        //test_m.text = data.dName
        m_dur_btn.setOnClickListener {
            var api = apiTask()
            api.execute(medicineDurApiUrl, medicineDurApiKey, mString)
            m_dur_btn.isEnabled = false
        }
    }

    // 복용 마지막 날짜를 계산하는 함수
    fun calculateEdate(medicines: ArrayList<MedicineVO>, sDate: String): String {
        // date format은 YYYY-MM-dd
        // medicines 중 투약일 수(total)가 제일 긴 것 = maxPeriod
        // sDate + maxPeriod
        var maxPeriod = -1
        for (medicineModel in medicines) {
            if (maxPeriod < medicineModel.total)
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
    }

    //공공데이터포털에서 제공하는 Dur 품목 api용
    inner class apiTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String? {
            val apiProc = ApiProc()
            return apiProc.getInfo(params[0], params[1], params[2])
        }

        override fun onPostExecute(result: String) {
            Log.d("api", "result:" + result)
            mDur(result)
        }
    }

    //Rds에 저장해 놓은 Dur 정보 가져와서 비교
    inner class rdsTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String? {
            val rdsProc = ServerProc()
            return rdsProc.start(params[0], params[1], params[2])
        }

        override fun onPostExecute(result: String?) {
            Log.d("dur Rds", result)
        }

    }
    fun mDur(result: String){
        var arr= result.split("%").toTypedArray()
        Log.d("api","resultSplit:"+arr[0])
        var item=ArrayList<CombineVO>()
        for(i in arr.indices){
            var temp=arr[i].split("@").toTypedArray()
            if(temp[1].contains("0")){//병용금기 의약품이 없을 경우

            }else{//병용금기 의약품이 존재

            }
        }
    }
    fun insertUserDurData(arr:MutableList<CombineVO>,dCode:String){
        //dur 테이블에 사용자가 새로 등록한 의약품과 병용판단한 결과 담기
        val pref : SharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val prefDid = pref.getString(SHARED_PREF_DID,"null")

        mLocalDatabase = FirebaseDatabase.getInstance()
        mRef = mLocalDatabase.getReference(TABLE_NAME)
        for(i in 0 until arr.size){
            //mRef.child(prefDid).child(dCode).child(arr[i].mName).setValue(arr[i])
        }

    }
    fun getUserMedicineData(){//디비에서 사용자가 등록한 질병이랑 약품 이름 가져오기
        val pref:SharedPreferences=getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val prefDid = pref.getString(SHARED_PREF_DID,"null")
        mLocalDatabase = FirebaseDatabase.getInstance()
        //mRef = mLocalDatabase.getReference("MEDICINE").

    }



}