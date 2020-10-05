package com.example.dyno.Server

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ApiProc {
    fun getInfo(url: String?, key: String?, medicines: String?): String? {
       // var arr = medicines?.split("/")?.toTypedArray()//병ㅇ
        var arr=listOf(medicines)
        var response=StringBuilder()
        val urlBuilder =
            StringBuilder("http://apis.data.go.kr/1470000/DURPrdlstInfoService/getUsjntTabooInfoList")
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + key)
        urlBuilder.append(
            "&" + URLEncoder.encode(
                "typeName",
                "UTF-8"
            ) + "=" + URLEncoder.encode("병용금기", "UTF-8")
        )
        if (arr != null) {
            for (i in arr.indices) {
                urlBuilder.append(
                    "&" + URLEncoder.encode(
                        "itemName",
                        "UTF-8"
                    ) + "=" + URLEncoder.encode(arr[i], "UTF-8")
                )
                try {
                    val url = URL(urlBuilder.toString())
                    val con: HttpURLConnection = url.openConnection() as HttpURLConnection

                    if (con != null) {
                        Log.d("api", "Connection Success")
                        con.requestMethod = "GET"
                        con.setRequestProperty("Content-type", "application/json")
                    } else {
                        Log.d("api", "Connection Fail")
                    }

                    val responesCode: Int = con.getResponseCode()
                    var rd:BufferedReader
                    if(responesCode>=200 && responesCode<=300){
                        rd = BufferedReader(InputStreamReader(con.inputStream))

                        var line:String?=null
                        while ({line=rd.readLine(); line}()!=null) {
                            response.append(line);
                        }
                        rd.close()
                        Log.d("api","response:"+response)
                    }else{
                        Log.d("api","responseCode"+responesCode.toString())
                        Log.d("api","responseCode:"+con.getErrorStream())
                    }
                } catch (e: Exception) {
                    Log.d("api",e.toString())
                }
            }

        }
        return response.toString()
    }
}