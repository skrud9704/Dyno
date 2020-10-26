package com.example.dyno.View.RegistMedicine

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.OCR.OcrParsing
import com.example.dyno.OCR.OcrProc
import com.example.dyno.R
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.OCR.CameraActivity
import com.example.dyno.VO.DiseaseGuessVO
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.MedicineVO
import com.example.dyno.View.Detail.DetailMedicineActivity
import kotlinx.android.synthetic.main.activity_regist_medicine.*
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
    private var medicines : ArrayList<MedicineVO> = arrayListOf()
    private lateinit var medicineAdapter : MedicineAdapter
    private lateinit var diseaseAdapter : DiseaseAdapter
    private var preType:Int=1;//1이면 약봉투 2면 처방전
    private var dcode:String=""
    private var dname:String=""
    private var disease:ArrayList<String> = arrayListOf()
    private var diseaseCode:ArrayList<String> = arrayListOf()


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
                Toast.makeText(applicationContext,"효능 효과 : ${medicines[position].effect}",Toast.LENGTH_SHORT).show()
            }

        })
        ocr_result_list.adapter = medicineAdapter
        ocr_result_list.layoutManager= LinearLayoutManager(this)

        diseaseAdapter = DiseaseAdapter(this,disease,diseaseCode)
        ocr_result_Dlist.adapter=diseaseAdapter
        ocr_result_Dlist.layoutManager = LinearLayoutManager(this)
        // 4. 질병 추측 -> DiseaseVO


        // 5 버튼 셋팅
        initButton()
    }

    private fun initFile(){
        imgFilePath = intent.getStringExtra("bitmapImg")
        imgFile = File(imgFilePath)
    }

    private fun initButton(){
        // 질병 상세 화면
        btn_detail_medicine.setOnClickListener {
            val intent = Intent(this, DetailMedicineActivity::class.java)
            if(preType==2)
                intent.putExtra("DATA_DISEASE", DiseaseVO(dcode,dname,"", medicines))
            else
                intent.putExtra("DATA_DISEASE", DiseaseVO("A000",dname,"", medicines))
            startActivity(intent)
            finish()
        }

        // 재촬영 버튼 (OCR결과 없어서)
        btn_reocr.setOnClickListener {
            val intent=Intent(this, CameraActivity::class.java)
            intent.putExtra("DATA","medicine")
            startActivity(intent)
            finish()
        }
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

        override fun onPostExecute(result: String?) {
            if (result != null) {
                Log.d("json", result.length.toString())
                val ocrPasredData = getResultParsed(result)
                if(preType==1){
                    getMedicine(ocrPasredData)
                }else{
                    getMedAndDisease(ocrPasredData)
                }
                medicineAdapter.notifyDataSetChanged()
                diseaseAdapter.notifyDataSetChanged()
            } else {
                Log.d("no_result", "")
            }
            ocr_result_progress.visibility = View.GONE
            ocr_result_list.visibility = View.VISIBLE
        }

        // 인식한 결과 파싱
        private fun getResultParsed(result: String) : String {
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
                val check:Boolean=translateText.contains("질병분류기호")
                val check1:Boolean=translateText.contains("질병")
                val check2:Boolean=translateText.contains("기호")

                var parsingResult:String=""
                if(check||check1||check2){//질병분류기호가 있을 경우 처방전
                    Log.d("pars_start","질병분류기호 있음")
                    preType=2
                    parsingResult= OcrParsing().prescriptionR(translateText)
                }else{//아닐경우 약봉투임
                    parsingResult= OcrParsing().prescriptionDrugsR(translateText)
                }
                return parsingResult
            } catch (e: Exception) {
                Log.d("parse_error", e.toString())
            }
            return ""
        }

        // OCR 된 문자열 -> 서버로부터 의약품 가져오기, 질병 추측 해오기
        private fun getMedicine(data : String){
            val retrofit = RetrofitClient.getInstance()
            val medicineService = retrofit.create(RetrofitService::class.java)
            Log.d(TAG,"서버로 보내는 데이터 : $data")

            // 서버 RDS에서 OCR파싱된 의약품 획득 후 질병 추측까지 완료.
            medicineService.requestMedicineAndDiseaseList(data).enqueue(object : retrofit2.Callback<DiseaseGuessVO> {
                override fun onFailure(call: Call<DiseaseGuessVO>, t: Throwable) {
                    Log.d(TAG,"실패 {$t}")
                    Log.d(TAG,"실패 {$call}")
                }

                override fun onResponse(call: Call<DiseaseGuessVO>, response: Response<DiseaseGuessVO>) {
                    Log.d(TAG,"성공^^")

                    if(response.body()!!.diseaseNameList.size<2){
                        guess_name_1st.text = "추측불가능"
                        guess_per_1st.text = ""

                        guess_name_2nd.text = "추측불가능"
                        guess_per_2nd.text = ""

                        guess_name_3rd.text = "추측불가능"
                        guess_per_3rd.text = ""
                    }else if(response.body()!!.diseaseNameList.size==2){
                        guess_name_1st.text = response.body()!!.diseaseNameList[0]
                        guess_per_1st.text = response.body()!!.diseasePerList[0]
                        guess_name_2nd.text = "추측불가능"
                        guess_per_2nd.text = ""

                        guess_name_3rd.text = "추측불가능"
                        guess_per_3rd.text = ""
                    }else if(response.body()!!.diseaseNameList.size==3){
                        dname=response.body()!!.diseaseNameList[0]
                        guess_name_1st.text = response.body()!!.diseaseNameList[0]
                        guess_per_1st.text = response.body()!!.diseasePerList[0]
                        guess_name_2nd.text = response.body()!!.diseaseNameList[1]
                        guess_per_2nd.text = response.body()!!.diseasePerList[1]

                        guess_name_3rd.text = "추측불가능"
                        guess_per_3rd.text = ""
                    }else{
                        dname=response.body()!!.diseaseNameList[0]
                        guess_name_1st.text = response.body()!!.diseaseNameList[0]
                        guess_per_1st.text = response.body()!!.diseasePerList[0]

                        guess_name_2nd.text = response.body()!!.diseaseNameList[1]
                        guess_per_2nd.text = response.body()!!.diseasePerList[1]

                        guess_name_3rd.text = response.body()!!.diseaseNameList[2]
                        guess_per_3rd.text = response.body()!!.diseasePerList[2]

                    }
                    // 1. 추측 질병 순위 UI 셋팅

                    guess.visibility = View.VISIBLE


                    // 2. OCR, 파싱, RDS 처리 완료된 의약품 리스트 UI 셋팅
                    for(medicine in response.body()!!.medicineList){
                        Log.d(TAG, "의약품 결과")
                        Log.d(TAG, medicine.name)
                        if(medicine.name!="Not found"){
                            medicines.add(medicine)
                            medicineAdapter.notifyDataSetChanged()
                            ocr_result_no.visibility = View.GONE
                            ocr_result_list.visibility = View.VISIBLE
                        }
                        medicineAdapter.notifyDataSetChanged()
                        if(medicines.size==0){
                            ocr_result_no.visibility = View.VISIBLE
                            ocr_result_list.visibility = View.GONE
                        }
                    }
                }
            })


        }

        //처방전을 ocr 하였을 경우-> 서버로부터 의약품이랑 질병명 가져오기
        private fun getMedAndDisease(data:String){
            val retrofit = RetrofitClient.getInstance()
            val medicineService = retrofit.create(RetrofitService::class.java)
            medicines.clear()

            Log.d(TAG,"서버로 보내는 데이터 질병 : $data")

            // 서버 RDS에서 OCR파싱된 의약품 획득 후 질병 추측까지 완료.
            medicineService.requestMAndDList(data).enqueue(object : retrofit2.Callback<DiseaseGuessVO> {
                override fun onFailure(call: Call<DiseaseGuessVO>, t: Throwable) {
                    Log.d(TAG,"실패 질병 {$t}")
                    Log.d(TAG,"실패 질병 {$call}")
                }

                override fun onResponse(call: Call<DiseaseGuessVO>, response: Response<DiseaseGuessVO>) {
                    Log.d(TAG,"질병 성공^^")

                    // 1. 추측 질병 순위 UI 셋팅

                    disease_text.text = "읽어온 질병"
                    for(code in response.body()!!.diseasePerList){
                        dcode+=code
                        diseaseCode.add(code)
                        diseaseAdapter.notifyDataSetChanged()
                    }
                    for(name in response.body()!!.diseaseNameList){
                        if(name!="Not found"){
                            dname+=name
                            disease.add(name)
                            diseaseAdapter.notifyDataSetChanged()
                            guess.visibility = View.GONE
                            ocr_result_Dlist.visibility = View.VISIBLE
                        }
                        diseaseAdapter.notifyDataSetChanged()
                        if(disease.size==0){
                            guess.visibility = View.VISIBLE
                            ocr_result_Dlist.visibility = View.GONE
                        }

                    }
                    for(code in response.body()!!.diseasePerList){
                        dcode+=code
                        diseaseCode.add(code)
                    }

                    if(response.body()!!.medicineList.size==0){
                        ocr_result_no.visibility = View.VISIBLE
                        ocr_result_list.visibility = View.GONE
                    }

                    // 2. OCR, 파싱, RDS 처리 완료된 의약품 리스트 UI 셋팅
                    for(medicine in response.body()!!.medicineList){
                        Log.d(TAG, "의약품 결과")
                        Log.d(TAG, medicine.name)
                        if(medicine.name!="Not found"){
                            medicines.add(medicine)
                            medicineAdapter.notifyDataSetChanged()
                            ocr_result_no.visibility = View.GONE
                            ocr_result_list.visibility = View.VISIBLE
                        }
                        medicineAdapter.notifyDataSetChanged()
                        if(medicines.size==0){
                            ocr_result_no.visibility = View.VISIBLE
                            ocr_result_list.visibility = View.GONE
                        }
                    }

                }
            })
        }

    }
}


