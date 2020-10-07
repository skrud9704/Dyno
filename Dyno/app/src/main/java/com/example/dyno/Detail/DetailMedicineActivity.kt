package com.example.dyno.Detail

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dyno.R
import com.example.dyno.Server.ApiProc
import com.example.dyno.Server.ServerProc
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.MedicineVO
import kotlinx.android.synthetic.main.activity_detail_medicine.*

class DetailMedicineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_medicine)

        val medicineDurApiUrl = "http://apis.data.go.kr/1470000/DURPrdlstInfoService"
        val medicineDurApiKey =
            "aK%2FBiBnzg6KzgMpSBaMjM7G42kbPdMd%2BOk9KPT8NOlGfDW5pRdlKhU2FufcZ4%2FlKFnHBVpi0gbVbfT8FDdTRhg%3D%3D"

        val data = intent.getParcelableExtra<DiseaseVO>("DATA")

        var medicines: MutableList<MedicineVO> = data.medicines
        Log.d("api", "medicines:" + medicines)

        var mString: String = ""//질병별 의약품 이름들 스트링에 붙이기
        var mNum: Int = medicines.size
        for (i in 0 until mNum) {
            mString += medicines[i].mName
            mString += "/"
        }
        mString = mString.substring(0, mString.length - 1)
        Log.d("api", "medicines:" + mString)
        test_m.text = data.dName
        m_dur_btn.setOnClickListener {
            var api = apiTask()
            api.execute(medicineDurApiUrl, medicineDurApiKey, mString)
        }
    }

    //공공데이터포털에서 제공하는 Dur 품목 api용
    inner class apiTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String? {
            val apiProc = ApiProc()
            return apiProc.getInfo(params[0], params[1], params[2])
        }

        override fun onPostExecute(result: String?) {

            Log.d("api", "result:" + result)
        }
    }

    //Rds에 저장해 놓은 Dur 정보 가져와서 비교
    inner class rdsTask:AsyncTask<String, String, String>(){
        override fun doInBackground(vararg params: String?): String? {
            val rdsProc=ServerProc()
            return rdsProc.start(params[0],params[1],params[2])
        }
        override fun onPostExecute(result:String?){
            Log.d("dur Rds",result)
        }

    }


}