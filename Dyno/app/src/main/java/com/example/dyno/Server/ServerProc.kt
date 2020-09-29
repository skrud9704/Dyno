package com.example.dyno.Server

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class ServerProc {
    fun start(phpUrl: String?, category: String?, drug: String?):String?{
        Log.d("url",phpUrl)
        var response=""
        Log.d("drugSer",drug)
        try{
            val url= URL(phpUrl)
            val con:HttpURLConnection=url.openConnection() as HttpURLConnection
            if(con!=null){//연결됨
                Log.d("Scon","connectionSuccess")
                con.requestMethod="POST"
                con.setRequestProperty("Accept-Charset", "UTF-8")
                con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8")
            }else{
                Log.d("Scon","connectionFail")
            }
            var str:String="category="+category+"&drug="+drug
            Log.d("cAndD",category+drug)
            val arr=str.toByteArray((Charset.defaultCharset()))

            val wr = DataOutputStream(con.outputStream)
            wr.write(arr)
            wr.flush()
            wr.close()
            val responseCode: Int = con.getResponseCode()
            Log.d("SresponseCode",responseCode.toString())
            if (responseCode == 200) { // 정상 호출
                /*System.out.println(con.getResponseMessage())*/
                Log.d("server_getInfo",con.responseMessage)
                val input = BufferedReader(
                    InputStreamReader(con.inputStream,"utf-8")
                )
                var inputLine: String?=null
                val response=StringBuffer()
                while ({inputLine=input.readLine();inputLine}()!=null){
                    response.append(inputLine)
                }
                input.close()
                return response.toString()

            } else {  // 에러 발생
                Log.d("SresponseCode",responseCode.toString())
                Log.d("SresponseCode",con.errorStream.toString())
            }
        }catch (e:Exception){
            Log.d("Serror",e.toString())
        }
        Log.d("SsqlResult",response)

        return response
    }
}