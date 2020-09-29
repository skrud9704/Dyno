package com.example.dyno.Server

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.dyno.RegistMedicine.ocrParsing
import org.json.JSONObject

class RdsServer {
    val url:String="http://15.164.144.36:80"

    inner class networkTask: AsyncTask<String, String, String>(){

        override fun doInBackground(vararg params: String?): String ?{


            //Log.d("networkTask",params[0])
            var category=params[0].toString()
            //의약품-drug인지 건강기능식품인지
            var drug=""//의약품 이름 ','로 php에서 split 해야함
            var temp=params[1]

            val serverProc=ServerProc()
            return serverProc.start(url,params[0],params[1])
        }
        override fun onPostExecute(result:String?){

            if (result != null) {
                Log.d("Sjson", result.length.toString())
                ReturnThreadResult(result,"drug")
                Log.d("Sresult",result.toString())
            } else {
                Log.d("no_result", "")
            }
        }
    }
    fun ReturnThreadResult(result: String,category: String)  {

        Log.d("start_serverS", result)
        var translateText: String? = ""
        val TAG_JSON = "webnautes"
        var TAG_NAME = ""
        var TAG_NUM = ""
        var TAG_DETAIL = ""
        var serverDrugInfo=""
        try {
            if(category=="drug"){
                TAG_NAME = "m_name"
                TAG_NUM = "m_effectNum"
                TAG_DETAIL = "m_guide"
            }
            val jsonObject = JSONObject(result)
            val jsonArray = jsonObject.getJSONArray(TAG_JSON)

            for (i in 0 until jsonArray.length()) {
                var item=jsonArray.getJSONObject(i)
                var name=item.getString(TAG_NAME)
                var effectNum=item.getString(TAG_NUM)
                var detail=item.getString(TAG_DETAIL)
                serverDrugInfo+=name+","+effectNum+","+detail+"/"

            }
            Log.d("get_end", serverDrugInfo)

        } catch (e: Exception) {
            Log.d("Server_error", e.toString())

        }

    }

}