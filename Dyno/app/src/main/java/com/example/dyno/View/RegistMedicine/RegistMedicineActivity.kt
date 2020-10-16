package com.example.dyno.View.MyPage.RegistMedicine

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.OCR.OcrParsing
import com.example.dyno.OCR.OcrProc
import com.example.dyno.R
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.MedicineVO
import com.example.dyno.View.MyPage.Detail.DetailMedicineActivity
import com.example.dyno.View.RegistMedicine.MedicineAdapter
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_regist_medicine.*
import kotlinx.android.synthetic.main.activity_regist_supplement.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import kotlin.collections.ArrayList

class RegistMedicineActivity : AppCompatActivity() {

    private val OCR_API_GW_URL =
        "https://4613fa1b45164de0814a2450c31bfc1c.apigw.ntruss.com/custom/v1/3398/2065ad05effce12ce5c7cb354380e6a13c219ae2f00c996d31988fe0eeb4c844/general"
    private val OCR_SECRET_KEY = "SGhKZ3pERXpHQnZWZEtpQlVaeHJqb2JoZWlVaUpWcW4="
    private lateinit var imgFilePath : String
    private lateinit var imgFile : File
    private val TAG = this::class.java.simpleName
    private val medicines : ArrayList<MedicineVO> = arrayListOf()
    private lateinit var medicineAdapter : MedicineAdapter


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_medicine)

        // 1. CameraActivity로부터 온 파일 저장
        initFile()

        // 2. OCR(글자인식) -> 3. Parsing -> 4. Server로부터 의약품 정보 획득. (순서대로일어나게) ArrayList<MedicineVO>
        ClovaOcrTask().execute(OCR_API_GW_URL,OCR_SECRET_KEY,imgFilePath)

        // 3. UI 뿌려지기
        medicineAdapter = MedicineAdapter(this,medicines)
        medicineAdapter.setListener(object : MedicineAdapter.diseaseClickListener{
            override fun onItemClick(position: Int) {

            }

        })
        ocr_result_list.adapter = medicineAdapter
        ocr_result_list.layoutManager= LinearLayoutManager(this)
        // 질병 추측 -> DiseaseVO

        // 질병 상세 화면
        btn_detail_medicine.setOnClickListener {
            val intent = Intent(this, DetailMedicineActivity::class.java)
            intent.putExtra("DATA_DISEASE", DiseaseVO("A000","아직 몰라","", medicines))
            startActivity(intent)
        }
    }

    private fun initFile(){
        imgFilePath = intent.getStringExtra("bitmapImg")
        imgFile = File(imgFilePath)
    }

    @SuppressLint("StaticFieldLeak")
    inner class ClovaOcrTask : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            ocr_result_progress.visibility = View.VISIBLE
            ocr_result_list.visibility = View.GONE
        }
        override fun doInBackground(vararg params: String): String? {
            return OcrProc()
                .start(params[0], params[1], params[2])
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                Log.d("json", result.length.toString())
                val ocrPasredData = getResultParsed(result)!!
                getMedicine(ocrPasredData)
                medicineAdapter.notifyDataSetChanged()
            } else {
                Log.d("no_result", "")
            }
            ocr_result_progress.visibility = View.GONE
            ocr_result_list.visibility = View.VISIBLE
        }

        // 인식한 결과 파싱
        private fun getResultParsed(result: String) : ArrayList<String>? {
            var translateText = ""
            try {
                // 인식된 결과를 Json으로 변환
                val jsonObject = JSONObject(result)
                // images -> fields -> inferText
                val jsonArray = jsonObject.getJSONArray("images")
                for (i in 0 until jsonArray.length()) {
                    val jsonArrayFields = jsonArray.getJSONObject(i).getJSONArray("fields")
                    for (j in 0 until jsonArrayFields.length()) {
                        val inferText = jsonArrayFields.getJSONObject(j).getString("inferText")
                        translateText += inferText
                        translateText += "/"
                    }
                }
                // 파싱된 결과를 리턴 ArrayList<String>
                return OcrParsing().prescriptionDrugsR(translateText)

            } catch (e: Exception) {
                Log.d("parse_error", e.toString())
            }
            return null
        }

        private fun getMedicine(data : ArrayList<String>){
            val retrofit = RetrofitClient.getInstance()
            val medicineService = retrofit.create(RetrofitService::class.java)

            for(d in data)
                Log.d(TAG,"데이터 - $d")

            for(i in data){
                medicineService.requestMedicineSingle(i).enqueue(object : retrofit2.Callback<MedicineVO> {
                    override fun onFailure(call: Call<MedicineVO>, t: Throwable) {
                        Log.d(TAG,"실패 {$t}")
                    }

                    override fun onResponse(call: Call<MedicineVO>, response: Response<MedicineVO>) {
                        Log.d(TAG,"성공^^")
                        Log.d(TAG, response.body()!!.name)
                        if(response.body()!!.name!="Not found") {
                            medicines.add(response.body()!!)
                            medicineAdapter.notifyDataSetChanged()
                        }
                    }
                })
            }


        }


    }
}