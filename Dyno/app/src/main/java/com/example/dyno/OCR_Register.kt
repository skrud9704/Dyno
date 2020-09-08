package com.example.dyno

import android.R
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_o_c_r__register.*
import org.json.JSONObject


class OCR_Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        val ocrApiGwUrl = "https://a9df7268db6249c5a4c39d027ef26ec6.apigw.ntruss.com/custom/v1/3398/2065ad05effce12ce5c7cb354380e6a13c219ae2f00c996d31988fe0eeb4c844/general"
        val ocrSecretKey="VnFodmVCeU9rYlhkVWtySHVMSVpCR09XeGZXUmZpeks="
        btn_ocr_translate.setOnClickListener{
            var task =PapagoNmTask()
            task.execute(ocrApiGwUrl,ocrSecretKey)
        }
    }

     inner  class PapagoNmTask : AsyncTask<String,String,String>(){
            override fun doInBackground(vararg params: String?): String {
                return OcrProc.start(params[0],params[1])
            }
         override fun onPostExecute(result: String?) {
             if (result != null) {
                 ReturnThreadResult(result)
             }
         }

     }

    fun ReturnThreadResult(result: String) {
        println("###  Retrun Thread Result")
        var translateText: String? = ""
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
            val txtResult = findViewById(R.id.textView_ocr_result) as TextView
            txtResult.text = translateText
        } catch (e: Exception) {
        }
    }


}