package com.example.dyno.RegistMedicine

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_ocr_register.*
import org.json.JSONObject
import java.io.File


class ocrRegister : AppCompatActivity() {
    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr_register)
        val ocrApiGwUrl = "https://4613fa1b45164de0814a2450c31bfc1c.apigw.ntruss.com/custom/v1/3398/2065ad05effce12ce5c7cb354380e6a13c219ae2f00c996d31988fe0eeb4c844/general"
        val ocrSecretKey = "UW5ERENERFZnR0p5bmdGa1hpUUNKUFpqZEFEa21MZ1A="
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
            var task=RdsServer().networkTask()
            task.execute("drug",text)
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


}