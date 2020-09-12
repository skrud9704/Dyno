package com.example.dyno.Server

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.dyno.R
import org.json.JSONObject

class ServerExample : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_example)
        val url:String="http://15.164.144.36:80"
        var task=networkTask()
        task.execute(url)
    }

    inner class networkTask:AsyncTask<String,String,String>(){
        override fun doInBackground(vararg params: String?): String ?{
            Log.d("networkTask",params[0])

            val serverProc=ServerProc()
            return serverProc.start(params[0])
        }
        override fun onPostExecute(result:String?){
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
        try {
            val txtResult = findViewById<TextView>(R.id.tv_outPut)
            txtResult.text = translateText
            Log.d("trans_end", translateText)
        } catch (e: Exception) {
            Log.d("pars_error", e.toString())
        }
    }

}