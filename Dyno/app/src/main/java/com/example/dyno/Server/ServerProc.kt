package com.example.dyno.Server

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class ServerProc {
    fun start(phpUrl:String?):String?{
        Log.d("url",phpUrl)
        var sqlResult=""

        try{
            val url= URL(phpUrl)
            val con:HttpURLConnection=url.openConnection() as HttpURLConnection
            if(con!=null){//연결됨
                Log.d("con","connectionSuccess")
                con.requestMethod="POST"
                con.setRequestProperty("Accept-Charset", "UTF-8")
                con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8")
            }else{
                Log.d("con","connectionFail")
            }
            var str:String="tjqjtleh"
            val arr=str.toByteArray((Charset.defaultCharset()))

            val wr = DataOutputStream(con.outputStream)
            wr.write(arr)
            wr.flush()
            wr.close()
            val responseCode: Int = con.getResponseCode()
            Log.d("responseCode",responseCode.toString())
            if (responseCode == 200) { // 정상 호출
                /*System.out.println(con.getResponseMessage())*/
                Log.d("trans_start",con.responseMessage)
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
                Log.d("responseCode",responseCode.toString())
                sqlResult = con.getResponseMessage()
            }
        }catch (e:Exception){
            Log.d("error",e.toString())
        }
        Log.d("sqlResult",sqlResult)
        return sqlResult

    }
}