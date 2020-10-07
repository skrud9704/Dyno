package com.example.dyno.RegistMedicine

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dyno.R
import com.example.dyno.RegistMedicine.OcrProc
import com.example.dyno.Server.RdsServer
import com.example.dyno.Server.ServerProc
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.MedicineVO
import com.example.dyno.VO.UserVO
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_ocr_register.*
import org.json.JSONObject
import java.io.File


class ocrRegister : AppCompatActivity() {
    private lateinit var mLocalDatabase : FirebaseDatabase
    private lateinit var mRef : DatabaseReference
    var TABLE_NAME = "MEDICINE"
    var SHARED_PREF = "SaveLogin"
    var SHARED_PREF_DID = "did"
    var medicineList=mutableListOf<MedicineVO>()

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr_register)

        val ocrApiGwUrl ="https://4613fa1b45164de0814a2450c31bfc1c.apigw.ntruss.com/custom/v1/3398/2065ad05effce12ce5c7cb354380e6a13c219ae2f00c996d31988fe0eeb4c844/general"
        val ocrSecretKey = "UUVJaWtUdnFvTlVjVm5ld2tjS1dEWHlzdWNPbFNnYUg="
        var filepath: String = ""

        if (intent.hasExtra("bitmapImg")) {//사진 저장된 로컬 저장소 주소 받아옴
            filepath = intent.getStringExtra("bitmapImg")
        } else {
            Log.d("noFile", null)
        }
        val file = File(filepath)
        if (Build.VERSION.SDK_INT < 28) {
            val bitmap = MediaStore.Images.Media
                .getBitmap(contentResolver, Uri.fromFile(file))
            imageView.setImageBitmap(bitmap)
        } else {
            val decode = ImageDecoder.createSource(
                this.contentResolver,
                Uri.fromFile(file)
            )
            val bitmap = ImageDecoder.decodeBitmap(decode)
            imageView.setImageBitmap(bitmap)
        }
        var drug:String=""
        btn_ocr_translate.setOnClickListener {
            Log.d("trans_start", ocrApiGwUrl)
            var task = PapagoNmTask()
            task.execute(ocrApiGwUrl, ocrSecretKey, filepath)

        }
        val txtResult = findViewById(R.id.textView_ocr_result) as TextView
        btn_server.setOnClickListener{
            var text=txtResult.getText().toString()
            Log.d("server_connect",text)
            var task=networkTask()
            val temp=task.execute("drug",text)
            Log.d("server_drug", temp.toString())

        }
        userD.setOnClickListener{
            println(medicineList)
            insertUserData(medicineList,"예시")
        }
    }

    inner class PapagoNmTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String? {
            Log.d("ocr_start", params[0])
            Log.d("ocr_start", params[2])
            val ocrProc = OcrProc()
            return ocrProc.start(params[0], params[1], params[2])
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                Log.d("json", result.length.toString())
                ReturnThreadResult(result)
            } else {
                Log.d("no_result", "")
            }
        }

    }
    fun ReturnThreadResult(result: String) {
        Log.d("start_pars", result)
        var translateText: String? = ""
        var parsText:String?=""
        try {
            val jsonObject = JSONObject(result)
            val jsonArray = jsonObject.getJSONArray("images")
            for (i in 0 until jsonArray.length()) {
                val jsonArray_fields = jsonArray.getJSONObject(i).getJSONArray("fields")
                for (j in 0 until jsonArray_fields.length()) {
                    val inferText =
                        jsonArray_fields.getJSONObject(j).getString("inferText")
                    translateText += inferText
                    translateText += " "
                }
            }

            Log.d("trans_end", translateText)
            if(translateText!=null){
                parsText=ocrParsing().prescriptionDrugsR(translateText)
            }
            val txtResult = findViewById(R.id.textView_ocr_result) as TextView
            Log.d("server_start",parsText)

            txtResult.text = parsText

        } catch (e: Exception) {
            Log.d("pars_error", e.toString())
        }

    }
    inner class networkTask: AsyncTask<String, String, String>(){
        var res:String=""
        val url:String="http://15.164.144.36:80"
        override fun doInBackground(vararg params: String?): String ?{


            //Log.d("networkTask",params[0])
            var category=params[0].toString()
            //의약품-drug인지 건강기능식품인지
            var drug=""//의약품 이름 ','로 php에서 split 해야함
            var temp=params[1]

            val serverProc= ServerProc()
            return serverProc.start(url,params[0],params[1])
        }
        override fun onPostExecute(result:String?){

            if (result != null) {
                Log.d("Sjson", result.length.toString())

                res=netReturnThreadResult(result,"drug")
                Log.d("Sresult",result.toString())
            } else {
                Log.d("no_result", "")
            }
        }
    }
    fun netReturnThreadResult(result: String,category: String) : String {
        Log.d("start_serverS", result.toString())
        var translateText: String? = ""
        val TAG_JSON = "webnautes"
        var TAG_NAME = ""
        var TAG_NUM = ""
        var TAG_DETAIL = ""
        var serverDrugInfo=""
        var TAG_CODE=""
        var TAG_INGREDIENT=""

        try {
            if(category=="drug"){
                TAG_NAME = "m_name"
                TAG_NUM = "m_effectNum"
                TAG_DETAIL = "m_guide"
                TAG_CODE="m_code"
            }
            val jsonObject = JSONObject(result)
            val jsonArray = jsonObject.getJSONArray(TAG_JSON)

            for (i in 0 until jsonArray.length()) {
                var item=jsonArray.getJSONObject(i)
                var name=item.getString(TAG_NAME)
                var effectNum=item.getString(TAG_NUM)
                var detail=item.getString(TAG_DETAIL)
                var code=item.getString(TAG_CODE)
                var ingredient=item.getString(TAG_INGREDIENT)
                var medicine=MedicineVO(name,1,1,detail,1)

                medicineList.add(medicine)
                serverDrugInfo+=name+","+effectNum+","+detail+"\n"
            }
            Log.d("get_end", serverDrugInfo)
            val txtResult = findViewById(R.id.textView_ocr_result) as TextView
            Log.d("server_start",serverDrugInfo)

            txtResult.text = serverDrugInfo
            return serverDrugInfo

        } catch (e: Exception) {
            Log.d("Server_error", e.toString())

        }
        return serverDrugInfo
    }
    fun insertUserData(arr:MutableList<MedicineVO>,dCode:String){
        val pref : SharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val prefDid = pref.getString(SHARED_PREF_DID,"null")

        mLocalDatabase = FirebaseDatabase.getInstance()
        mRef = mLocalDatabase.getReference(TABLE_NAME)  // "USERS"
        for(i in 0 until arr.size){
            mRef.child(prefDid).child(dCode).child(arr[i].mName).setValue(arr[i])
        }

    }

}