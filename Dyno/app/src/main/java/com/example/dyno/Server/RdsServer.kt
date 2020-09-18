package com.example.dyno.Server

import android.os.AsyncTask
import android.util.Log

class RdsServer {
    val url:String="http://15.164.144.36:80"

    inner class networkTask: AsyncTask<String, String, String>(){
        override fun doInBackground(vararg params: String?): String ?{

            //Log.d("networkTask",params[0])

            val serverProc=ServerProc()
            return serverProc.start(url)
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
            Log.d("trans_end", translateText)
        } catch (e: Exception) {
            Log.d("pars_error", e.toString())
        }
    }

}