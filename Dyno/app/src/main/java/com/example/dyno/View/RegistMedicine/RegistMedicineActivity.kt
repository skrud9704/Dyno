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

    // 태그
    private val TAG = this::class.java.simpleName

    // OCR API 관련 URL, KEY
    private val OCR_API_GW_URL =
        "https://4613fa1b45164de0814a2450c31bfc1c.apigw.ntruss.com/custom/v1/3398/2065ad05effce12ce5c7cb354380e6a13c219ae2f00c996d31988fe0eeb4c844/general"
    private val OCR_SECRET_KEY = "SGhKZ3pERXpHQnZWZEtpQlVaeHJqb2JoZWlVaUpWcW4="

    // 앞선 CameraActivity로부터 가져온 OCR할 이미지 정보
    private lateinit var imgFilePath : String
    private lateinit var imgFile : File

    // OCR 인식, 파싱 후 서버로부터 정보 획득한 의약품 리스트
    private var medicines : ArrayList<MedicineVO> = arrayListOf()
    // 의약품 리스트 어댑터
    private lateinit var medicineAdapter : MedicineAdapter
    // 질병 추측, 획득 질병 리스트 어댑터
    private lateinit var diseaseAdapter : DiseaseAdapter

    // 인식한 사진이 의약품인지 처방전인지 판단
    private var preType:Int=1;      //1이면 약봉투 2면 처방전

    private var diseaseGuessVO : DiseaseGuessVO = DiseaseGuessVO()

    private var dcode:String=""
    private var dname:String=""


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_medicine)

        // 1. CameraActivity로부터 온 파일 저장
        initFile()

        // 2. OCR(글자인식) -> 3. Parsing -> 4. Server로부터 의약품 정보 획득. -> 5. 질병 추측 (순서대로일어나게) ArrayList<MedicineVO>
        ClovaOcrTask().execute(OCR_API_GW_URL,OCR_SECRET_KEY,imgFilePath)

        // 6. UI 뿌려지기
        medicineAdapter = MedicineAdapter(this,medicines)
        medicineAdapter.setListener(object : MedicineAdapter.diseaseClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext,"효능 효과 : ${medicines[position].effect}",Toast.LENGTH_SHORT).show()
            }
        })
        ocr_result_list.adapter = medicineAdapter
        ocr_result_list.layoutManager= LinearLayoutManager(this)

        diseaseAdapter = DiseaseAdapter(this,diseaseGuessVO)
        ocr_result_Dlist.adapter=diseaseAdapter
        ocr_result_Dlist.layoutManager = LinearLayoutManager(this)


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
                intent.putExtra("DATA_DISEASE", DiseaseVO("A000","TEMP","", medicines))
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

        // Task를 수행하기 전에 액티비티 LifeCycle이랑 분리되는 부분
        override fun onPreExecute() {
            super.onPreExecute()

            // Progress Bar START
            ocr_result_progress.visibility = View.VISIBLE
            ocr_result_list.visibility = View.GONE
        }

        override fun doInBackground(vararg params: String): String? {
            return OcrProc()
                .start(params[0], params[1], params[2])
        }

        // Task를 수행이 끝나서 액티비티 LifeCycle이랑 합쳐지는 부분
        override fun onPostExecute(result: String?) {
            // OCR 결과 데이터
            if (result != null) {
                // Log.d("json", result.length.toString())
                // 파싱 결과 데이터
                val ocrPasredData = getResultParsed(result)

                // "질병분류기호"를 포함하지 않은 경우 -> 약봉투로 인식 -> 의약품 획득 & 질병추측
                if(preType==1){
                    getMedicineAndGuess(ocrPasredData,preType)
                }
                // "질병분류기호"를 포함한 경우 -> 처방전으로 인식 -> 의약품, 질병 획득
                else if(preType==2){
                    getMedicineAndDisease(ocrPasredData,preType)
                }

            } else {
                Log.d(TAG, "Clova OCR 결과 없음.")
            }

            // Progress Bar END
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

                val parsingResult : String =
                    if(check||check1||check2){//질병분류기호가 있을 경우 처방전
                        Log.d(TAG,"파싱 결과 : 처방전")
                        preType=2
                        OcrParsing().prescriptionR(translateText)
                    }else{//아닐경우 약봉투임
                        Log.d(TAG,"파싱 결과 : 약봉투")
                        preType=1
                        OcrParsing().prescriptionDrugsR(translateText)
                    }
                Log.d(TAG,"파싱 결과 String : $parsingResult")
                return parsingResult
            } catch (e: Exception) {
                Log.d(TAG, "파싱 결과 : 에러 $e")
            }
            return ""
        }

        // 약봉투를 ocr 하였을 경우-> 서버로부터 의약품, 질병추측
        // OCR 된 문자열 -> 서버로부터 의약품 가져오기, 질병 추측 해오기
        private fun getMedicineAndGuess(data : String, type: Int){
            val retrofit = RetrofitClient.getInstance()
            val medicineService = retrofit.create(RetrofitService::class.java)
            Log.d(TAG,"서버로 보내는 데이터 : $data")

            if(data==""){
                ocr_result_no.visibility = View.VISIBLE
                ocr_result_list.visibility = View.GONE
            }else{
                // 서버 RDS에서 OCR파싱된 의약품 획득 후 질병 추측까지 완료.
                medicineService.requestMedicineAndDiseaseList(data,type).enqueue(object : retrofit2.Callback<DiseaseGuessVO> {
                    override fun onFailure(call: Call<DiseaseGuessVO>, t: Throwable) {
                        Log.d(TAG,"실패 {$t}")
                        Log.d(TAG,"실패 {$call}")
                        ocr_result_no.visibility = View.VISIBLE
                        ocr_result_list.visibility = View.GONE
                    }

                    override fun onResponse(call: Call<DiseaseGuessVO>, response: Response<DiseaseGuessVO>) {
                        Log.d(TAG,"성공^^")

                        // 1. 추측 질병 순위 UI 셋팅
                        diseaseGuessVO = response.body()!!
                        diseaseAdapter.setAdapterData(diseaseGuessVO)


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

        //처방전을 ocr 하였을 경우-> 서버로부터 의약품이랑 질병명 가져오기
        private fun getMedicineAndDisease(data:String, type:Int){
            val retrofit = RetrofitClient.getInstance()
            val medicineService = retrofit.create(RetrofitService::class.java)
            medicines.clear()

            Log.d(TAG,"서버로 보내는 데이터 질병 : $data")

            // 서버 RDS에서 OCR파싱된 의약품 획득 후 질병 추측까지 완료.
            medicineService.requestMedicineAndDiseaseList(data,type).enqueue(object : retrofit2.Callback<DiseaseGuessVO> {
                override fun onFailure(call: Call<DiseaseGuessVO>, t: Throwable) {
                    Log.d(TAG,"실패 질병 {$t}")
                    Log.d(TAG,"실패 질병 {$call}")
                }

                override fun onResponse(call: Call<DiseaseGuessVO>, response: Response<DiseaseGuessVO>) {
                    Log.d(TAG,"질병 성공^^")

                    // 1. 추측 질병 순위 UI 셋팅

                    disease_text.text = "읽어온 질병"
                    diseaseGuessVO = response.body()!!
                    diseaseAdapter.setAdapterData(diseaseGuessVO)

                    for(code in response.body()!!.diseasePerList){
                        dcode+=code
                        dcode+=" "

                    }
                    for(name in response.body()!!.diseaseNameList){
                        dname+=name
                        dname+=" "
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


