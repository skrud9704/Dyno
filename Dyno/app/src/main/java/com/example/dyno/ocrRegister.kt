package com.example.dyno

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_ocr_register.*
import org.json.JSONObject
import java.io.File


class ocrRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr_register)
        val ocrApiGwUrl = "url 넣을 것!"
        val ocrSecretKey="test할때 secretKey 넣을 것"
        var filepath:String=""
        if(intent.hasExtra("bitmapImg")){
            filepath=intent.getStringExtra("bitmapImg")
        }else{
            Log.d("noFile",null)
        }
        val file = File(filepath)
        if (Build.VERSION.SDK_INT < 28) {
            val bitmap = MediaStore.Images.Media
                .getBitmap(contentResolver, Uri.fromFile(file))
            imageView.setImageBitmap(bitmap)
        }
        else{
            val decode = ImageDecoder.createSource(this.contentResolver,
                Uri.fromFile(file))
            val bitmap = ImageDecoder.decodeBitmap(decode)
            imageView.setImageBitmap(bitmap)
        }

        btn_ocr_translate.setOnClickListener{
            Log.d("trans_start",ocrApiGwUrl)
            var task =PapagoNmTask()
            task.execute(ocrApiGwUrl,ocrSecretKey,filepath)
        }
    }

     inner  class PapagoNmTask : AsyncTask<String,String,String>(){
            override fun doInBackground(vararg params: String?): String? {
                Log.d("ocr_start",params[0])
                Log.d("ocr_start",params[2])
                val ocrProc=OcrProc()
                return ocrProc.start(params[0],params[1],params[2])
            }
         override fun onPostExecute(result: String?) {
             if (result != null) {
                 Log.d("json",result.length.toString())
                 ReturnThreadResult(result)
             }
             else{
                 Log.d("no_result","")
             }
         }

     }

    fun ReturnThreadResult(result: String) {
        Log.d("start_pars",result)
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
            Log.d("trans_end",translateText)
        } catch (e: Exception) {
            Log.d("pars_error",e.toString())
        }
    }


}